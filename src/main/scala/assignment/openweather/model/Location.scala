/*
 * Personal Practice
 */

package assignment.openweather.model

import io.circe._
import io.circe.generic.semiauto._
import cats.implicits._
import assignment.openweather.model.ApiError.LatLonDataError
/**
  *  A location with lat and lng
  * @param lat
  * @param lon
  */
final case class Location(lat: Double, lon: Double)

object Location {

  implicit val decode: Decoder[Location] = deriveDecoder[Location]

  implicit val encode: Encoder[Location] = deriveEncoder[Location]
  def validate(location: Location): Either[LatLonDataError, Location] =
    (validateLatitude(location.lat), validateLongitude(location.lon)).mapN(Location.apply)

  private def validateLatitude(lat: Double): Either[LatLonDataError, Double] =
    if (-90.0 <= lat && lat <= 90.0) Right(lat)
    else Left(LatLonDataError(s"Latitude must be between -90.0 and 90.0, but was $lat"))

  private def validateLongitude(lon: Double): Either[LatLonDataError, Double] =
    if (-180.0 <= lon && lon <= 180.0) Right(lon)
    else Left(LatLonDataError(s"Longitude must be between -180.0 and 180.0, but was $lon"))
}
