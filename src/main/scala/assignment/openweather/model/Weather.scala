/*
 * Personal Practice
 */

package assignment.openweather.model

import io.circe.generic.semiauto.deriveEncoder
import io.circe.{ Decoder, Encoder, HCursor }
import java.time.{ Instant, ZoneId, ZonedDateTime }
import java.time.format.DateTimeFormatter

case class WeatherReport(
    temperature: Double,
    feel: String,
    utcDateTime: String,
    weather: Option[String],
    description: Option[String]
)
object WeatherReport {
  implicit val encoder: Encoder[WeatherReport] = deriveEncoder[WeatherReport]
}
case class Weather(id: Int, main: String, description: String, icon: String)

case class WeatherMain(
    temperature: Double,
    name: String,
    dateTime: String,
    weatherMain: Option[String],
    description: Option[String]
)

object WeatherMain {
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")

  implicit val decodeWeather: Decoder[Weather] = (c: HCursor) =>
    for {
      id          <- c.downField("id").as[Int]
      main        <- c.downField("main").as[String]
      description <- c.downField("description").as[String]
      icon        <- c.downField("icon").as[String]
    } yield Weather(id, main, description, icon)

  implicit val decoder: Decoder[WeatherMain] = (hCursor: HCursor) =>
    for {
      temperature <- hCursor.downField("main").downField("temp").as[Double]
      place       <- hCursor.downField("name").as[String]
      epoch       <- hCursor.downField("dt").as[Long]
      weather     <- hCursor.downField("weather").as[Seq[Weather]].map(_.headOption)
      dateTime    = ZonedDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.of("UTC"))
      dateTimeStr = dateTime.format(formatter)
    } yield WeatherMain(
      temperature,
      place,
      dateTimeStr,
      weather.map(_.main),
      weather.map(_.description)
    )
}
