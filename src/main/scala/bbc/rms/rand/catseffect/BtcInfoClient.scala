package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.BtcInfoClient.{Coin, CoinRequestError}
import bbc.rms.rand.catseffect.CatInfoClient.CatFact
import bbc.rms.rand.catseffect.InfoClient.Info
import bbc.rms.rand.catseffect.RandDConfig.HttpClientConfig
import cats.effect.Bracket
import cats.implicits._
import io.circe.generic.auto._
import org.http4s.Method._
import org.http4s._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl

import scala.util.control.NoStackTrace

object BtcInfoClient {
  case class Coin() extends Info
  case class CoinRequestError(cause: String) extends NoStackTrace
}

final class BtcInfoClient[F[_]: JsonDecoder: Bracket[F, Throwable]](btcConfig: HttpClientConfig, client: Client[F])
    extends InfoClient[F, Coin] with Http4sClientDsl[F] {

  override def process(): F[Seq[Coin]] =
    GET(btcConfig.baseUrl).flatMap(
      req =>
        client.run(req).use { response =>
          if (response.status == Status.Ok)
            response.asJsonDecode[Seq[Coin]]
          else CoinRequestError(Option(response.status.reason).getOrElse("unknown")).raiseError[F, Seq[Coin]]
        }
    )
}
