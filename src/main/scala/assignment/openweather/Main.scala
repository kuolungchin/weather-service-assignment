/*
 * Personal Practice
 */

package assignment.openweather

import cats.implicits._
import cats.effect._

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val serverStream = ApiServer.serve[IO].attempt.map {
      case Left(e) =>
        IO {
          println("*** An Error Occurred! ***")
          if (e ne null) {
            System.err.println(e.getMessage)
          }
          ExitCode.Error
        }
      case Right(r) => r
    }
    serverStream.compile.drain.as(ExitCode.Success)
  }
}
