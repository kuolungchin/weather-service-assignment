/*
 * Personal Practice
 */

package assignment.openweather.model

import io.circe._
import io.circe.generic.semiauto._
import cats.implicits._
import assignment.openweather.model.{ Error => WeatherAPIError }
import assignment.openweather.model.ApiError.{InvalidEmailError, LatLonDataError}

import scala.util.matching.Regex

final case class WeatherRequestPayload(lat: Double, lon: Double, userEmail: String)

object WeatherRequestPayload {

  implicit val decode: Decoder[WeatherRequestPayload] = deriveDecoder[WeatherRequestPayload]

  implicit val encode: Encoder[WeatherRequestPayload] = deriveEncoder[WeatherRequestPayload]

  private val emailRegex: Regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".r

  def validate(location: WeatherRequestPayload): Either[WeatherAPIError, WeatherRequestPayload] =
    (
      validateLatitude(location.lat),
      validateLongitude(location.lon),
      validateEmail(location.userEmail)
    ).mapN(WeatherRequestPayload.apply)

  private def validateLatitude(lat: Double): Either[WeatherAPIError, Double] =
    if (-90.0 <= lat && lat <= 90.0) Right(lat)
    else Left(LatLonDataError(s"Latitude must be between -90.0 and 90.0, but was $lat"))

  private def validateLongitude(lon: Double): Either[WeatherAPIError, Double] =
    if (-180.0 <= lon && lon <= 180.0) Right(lon)
    else Left(LatLonDataError(s"Longitude must be between -180.0 and 180.0, but was $lon"))

  private def validateEmail(email: String): Either[WeatherAPIError, String] =
    email match {
      case emailRegex() => Right(email)
      case _            => Left(InvalidEmailError(s"Your Email, $email,is not a valid email address"))
    }
}
