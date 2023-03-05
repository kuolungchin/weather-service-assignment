/*
 * Personal Practice
 */

package assignment.openweather.model

sealed trait Error                                extends Exception
case class LocationError(msg: String)             extends Error
case class BadRequestError(msg: String)           extends Error
case class WeatherApiHttpClientError(msg: String) extends Error
case class NotificationError(msg: String)         extends Error
case class LatLonDataError(msg: String)           extends Error
