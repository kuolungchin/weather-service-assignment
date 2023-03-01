/*
 * Personal Practice
 */

package assignment.openweather.config

import assignment.openweather.{ NonEmptyString }
import eu.timepit.refined.auto._
import eu.timepit.refined.pureconfig._
import pureconfig._
import pureconfig.generic.semiauto._

/**
  * The configurations for API Server and Endpoints
  *
  * @param host The hostname / ip address
  * @param port The port number
  */
final case class ClientConfig(host: NonEmptyString, appid: NonEmptyString)

object ClientConfig {
  implicit val configReader: ConfigReader[ClientConfig] = deriveReader[ClientConfig]
}
