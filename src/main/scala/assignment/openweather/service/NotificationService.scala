/*
 * Personal Practice
 */

package assignment.openweather.service

import assignment.openweather.model
import cats.effect.Sync

trait NotificationService[F[_]] {
  val notificationServiceModule: Service

  trait Service {
    def sendNotification(memberId: String, text: String): F[Either[model.Error, Unit]]
  }
}

class LiveNotificationService[F[_]: Sync] extends NotificationService[F] {
  override val notificationServiceModule: Service = new Service {
    override def sendNotification(memberId: String, text: String): F[Either[model.Error, Unit]] =
      // TODO: for practice purpose only
      Sync[F].delay {
        Right(
          println(s"********* Sending a notification to ${memberId} with text: ${text} *********")
        )
      }
  }
}
