/*
 * Personal Practice
 */

package assignment.openweather.model

sealed trait Error extends Exception

object ApiError {
  case class WeatherApiHttpClientError(msg: String) extends Error
  case class NotificationError(msg: String)          extends Error
  case class LatLonDataError(msg: String)           extends Error
  case class InvalidEmailError(msg: String)         extends Error
}
