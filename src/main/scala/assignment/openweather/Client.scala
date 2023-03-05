/*
 * Personal Practice
 */

package assignment.openweather

import assignment.openweather.config.ClientConfig
import assignment.openweather.model.{ WeatherApiHttpClientError, WeatherMain }
import cats.effect._
import org.http4s.client.Client
import org.http4s._
import org.http4s.circe._
import cats.data.EitherT
import cats.implicits._

class WeatherApiClient[F[_]: Concurrent](client: Client[F], clientConfig: ClientConfig) {
  implicit val weatherMainDecoder: EntityDecoder[F, WeatherMain] = jsonOf[F, WeatherMain]
  private val clientHostName                                     = clientConfig.host
  private val appId                                              = clientConfig.appid
  def getWeatherInfo(lat: Double, lon: Double): EitherT[F, model.Error, WeatherMain] = {
    val uri = Uri
      .fromString(
        s"http://${clientHostName}/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${appId}&units=imperial"
      )
      .toOption
      .get
    val request = Request[F](Method.GET, uri)
    EitherT[F, model.Error, WeatherMain](client.fetch(request) { response =>
      response.as[WeatherMain].map(Right(_))
    }).leftMap(
      _ => WeatherApiHttpClientError("Fail to parse Weather Api Json")
    )
  }
}
