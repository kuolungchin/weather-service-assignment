/*
 * Personal Practice
 */

package assignment.openweather

import assignment.openweather.api.WeatherApiServiceImpl
import assignment.openweather.config.{ ApiConfig, ClientConfig }
import assignment.openweather.service.LiveWeatherConditionService
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import cats.effect._
import com.typesafe.config.ConfigFactory
import fs2._
import pureconfig.loadConfigOrThrow

import scala.concurrent.ExecutionContext.global

object ApiServer {
  def serve[F[_]](implicit Effect: ConcurrentEffect[F], T: Timer[F]): Stream[F, ExitCode] =
    for {
      apiConfig <- Stream.eval(Sync[F].delay {
        val cfg = ConfigFactory.load(getClass().getClassLoader())
        loadConfigOrThrow[ApiConfig](cfg, "api")
      })
      clientConfig <- Stream.eval(Sync[F].delay {
        val cfg = ConfigFactory.load(getClass().getClassLoader())
        loadConfigOrThrow[ClientConfig](cfg, "client")
      })
      _      <- Stream.eval(Sync[F].delay(println("Starting Http4s Client")))
      client <- Stream.resource(BlazeClientBuilder[F](global).resource)
      service = new WeatherApiServiceImpl(
        new LiveWeatherConditionService(new WeatherApiClient[F](client, clientConfig))
      )
      exitCode <- BlazeServerBuilder[F]
        .bindHttp(apiConfig.port.value, apiConfig.host.value)
        .withHttpApp(service.routes.orNotFound)
        .serve
    } yield exitCode
}
