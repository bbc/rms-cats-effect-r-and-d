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
        def catFacts: InfoClient[F, CatFact] = new CatInfoClient[F](clientAConfig, client)
        def btcInfo: InfoClient[F, Coin]     = new BtcInfoClient[F](clientBConfig, client)
      }
    )
}

trait HttpClients[F[_]] {
  def catFacts: InfoClient[F, CatFact]
  def btcInfo: InfoClient[F, Coin]
}
