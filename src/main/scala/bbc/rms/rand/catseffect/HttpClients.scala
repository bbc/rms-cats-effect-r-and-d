package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.RandDConfig.HttpClientConfig
import cats.effect._
import org.http4s.client.Client

object HttpClients {
  def make[F[_]: Sync](
                        clientAConfig: HttpClientConfig,
                        clientBConfig: HttpClientConfig,
                        client: Client[F]
                      ): F[HttpClients[F]] =
    Sync[F].delay(
      new HttpClients[F] {
        def payment: PaymentClient[F] = new LivePaymentClient[F](cfg, client)
      }
    )
}

trait HttpClients[F[_]] {
  def payment: PaymentClient[F]
}
