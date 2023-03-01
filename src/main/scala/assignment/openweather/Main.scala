/*
 * Personal Practice
 */

package assignment.openweather

import cats.implicits._
import cats.effect._

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    ApiServer.serve[IO].compile.drain.as(ExitCode.Success)
}
