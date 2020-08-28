package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.InfoClient.Info
import bbc.rms.rand.catseffect.RandDConfig.HttpClientConfig
import cats.effect.Bracket
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl

case class CatFact() extends Info

final class CatInfoClient[F[_]: Bracket[F, Throwable]](catFactConfig: HttpClientConfig,
                                                       client: Client[F])
  extends InfoClient[F, CatFact] with Http4sClientDsl[F] {

  override def process(): F[Seq[CatFact]] = ???
}
