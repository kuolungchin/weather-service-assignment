/*
 * Personal Practice
 */

package assignment.openweather.api

import cats.effect.Sync
import cats.implicits._
import cats.data._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import assignment.openweather.model._
import assignment.openweather.model.Location._
import assignment.openweather.service.WeatherConditionService
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import assignment.openweather.model.WeatherReport._

final class WeatherApiServiceImpl[F[_]: Sync](weatherConditionService: WeatherConditionService[F])
    extends Http4sDsl[F] {

  implicit def decodeProduct: EntityDecoder[F, Location] = jsonOf

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ GET -> Root / "weather" =>
      val result = for {
        location          <- EitherT.liftF(req.as[Location])
        validatedLocation <- EitherT.fromEither[F](Location.validate(location))
        weatherMain       <- weatherConditionService.getWeatherCondition(validatedLocation)
      } yield weatherMain

      result.value
        .flatMap {
          case Right(weatherMain)         => Ok(weatherMain)
          case Left(LatLonDataError(msg)) => BadRequest(msg)
          case Left(_)                    => BadRequest("Bad Request")
        }
        .handleErrorWith {
          case MalformedMessageBodyFailure(details, _) => BadRequest(details)
          case ex                                      => BadRequest("Please valid your request or API Key (appid) under resources/application.conf")
        }
  }
}
