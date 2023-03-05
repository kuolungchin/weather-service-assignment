/*
 * Personal Practice
 */

package assignment.openweather.service

import assignment.openweather.WeatherApiClient
import cats.effect.Sync
import assignment.openweather.model.{ Location, WeatherReport, Error => WeatherAPIError }
import cats.data.{ EitherT, Kleisli }

trait WeatherConditionService[F[_]] {
  def getWeatherCondition(location: Location): EitherT[F, WeatherAPIError, WeatherReport]
}

final class LiveWeatherConditionService[F[_]: Sync](
    weatherApiClient: WeatherApiClient[F],
    notificationService: NotificationService[F]
) extends WeatherConditionService[F] {
  override def getWeatherCondition(location: Location): EitherT[F, WeatherAPIError, WeatherReport] =
    for {
      weather <- weatherApiClient.getWeatherInfo(location.lat, location.lon)
      _       <- EitherT.liftF(sendNotification().run(notificationService))
      condition = if (weather.temperature < 36.00)
        WeatherReport(
          weather.temperature,
          "Cold",
          weather.dateTime,
          weather.weatherMain,
          weather.description
        )
      else if (weather.temperature < 70.00)
        WeatherReport(
          weather.temperature,
          "Mild",
          weather.dateTime,
          weather.weatherMain,
          weather.description
        )
      else
        WeatherReport(
          weather.temperature,
          "Hot",
          weather.dateTime,
          weather.weatherMain,
          weather.description
        )
    } yield condition

  def sendNotification(): Kleisli[F, NotificationService[F], Unit] =
    Kleisli { notificationService: NotificationService[F] =>
      notificationService.notificationServiceModule
        .sendNotification("administrator", "someone access this API")
    }
}
