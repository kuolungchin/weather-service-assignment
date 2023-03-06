/*
 * Personal Practice
 */

package assignment.openweather.api

import assignment.openweather.ErrorOr
import assignment.openweather.model.ApiError._
import cats.effect.Sync
import cats.implicits._
import cats.data._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import assignment.openweather.model._
import assignment.openweather.model.WeatherRequestPayload._
import assignment.openweather.service.{ NotificationService, WeatherConditionService }
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import assignment.openweather.model.WeatherReport._

final class WeatherApiServiceImpl[F[_]: Sync](
    weatherConditionService: WeatherConditionService[F],
    notificationService: NotificationService[F]
) extends Http4sDsl[F] {

  implicit def decodeProduct: EntityDecoder[F, WeatherRequestPayload] = jsonOf

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ GET -> Root / "weather" =>
      val result = for {
        payload <- EitherT.liftF(req.as[WeatherRequestPayload])
        _       <- EitherT.fromEither[F](WeatherRequestPayload.validate(payload))
        location = Location(payload.lat, payload.lon)
        weatherMain <- weatherConditionService.getWeatherCondition(location)
        _           <- EitherT(sendNotification().run(notificationService))
        // sendNotification().run(...) returns a F[ErrorOr[Unit]] directly,
        // so we can wrap it in EitherT.
      } yield weatherMain

      result.value
        .flatMap {
          case Right(weatherMain) => Ok(weatherMain)
          case Left(err) => {
            err match {
              case WeatherApiHttpClientError(msg) =>BadRequest(msg)
              case NotificationError(msg) =>BadRequest(msg)
              case LatLonDataError(msg) =>BadRequest(msg)
              case InvalidEmailError(msg) =>BadRequest(msg)
            }
          }
        }
        .handleErrorWith {
          case err       => BadRequest("Bad Request")
        }
  }

  private def sendNotification(): Kleisli[F, NotificationService[F], ErrorOr[Unit]] =
    Kleisli { notificationService: NotificationService[F] =>
      notificationService.notificationServiceModule
        .sendNotification("administrator", "someone access this API")
    }
}
