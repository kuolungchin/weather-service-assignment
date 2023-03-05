/*
 * Personal Practice
 */

package assignment.openweather.service

import cats.effect.Sync

trait NotificationService[F[_]] {
  val notificationServiceModule: Service

  trait Service {
    def sendNotification(memberId: String, text: String): F[Unit]
  }
}

class LiveNotificationService[F[_]: Sync] extends NotificationService[F] {
  override val notificationServiceModule: Service = new Service {
    override def sendNotification(memberId: String, text: String): F[Unit] =
      Sync[F].delay {
        println(s"Sending a notification to ${memberId} with text: ${text}")
      }
  }
}
