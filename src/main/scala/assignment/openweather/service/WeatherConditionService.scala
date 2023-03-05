/*
 * Personal Practice
 */

package assignment.openweather.service

import assignment.openweather.{ WeatherApiClient }
import cats.effect.Sync
import assignment.openweather.model.{ Location, WeatherReport, Error => WeatherAPIError }
import cats.data.EitherT

trait WeatherConditionService[F[_]] {
  def getWeatherCondition(location: Location): EitherT[F, WeatherAPIError, WeatherReport]
}

final class LiveWeatherConditionService[F[_]: Sync](
    weatherApiClient: WeatherApiClient[F]
) extends WeatherConditionService[F] {

  override def getWeatherCondition(location: Location): EitherT[F, WeatherAPIError, WeatherReport] =
    for {
      weather <- weatherApiClient.getWeatherInfo(location.lat, location.lon)
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
}
