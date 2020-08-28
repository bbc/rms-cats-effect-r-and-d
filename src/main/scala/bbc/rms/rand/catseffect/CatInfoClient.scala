package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.CatInfoClient.{CatFact, CatRequestError}
import bbc.rms.rand.catseffect.InfoClient.Info
import bbc.rms.rand.catseffect.RandDConfig.HttpClientConfig
import cats.effect.Bracket
import cats.implicits._
import io.circe.generic.auto._
import org.http4s.Method._
import org.http4s._
import org.http4s.circe._
import org.http4s.client._
import org.http4s.client.dsl.Http4sClientDsl

import scala.util.control.NoStackTrace

object CatInfoClient {
  case class CatFact() extends Info
  case class CatRequestError(cause: String) extends NoStackTrace
}

final class CatInfoClient[F[_]: JsonDecoder: Bracket[F, Throwable]](catFactConfig: HttpClientConfig, client: Client[F])
    extends InfoClient[F, CatFact] with Http4sClientDsl[F] {

  override def process(): F[Seq[CatFact]] =
    GET(catFactConfig.baseUrl).flatMap(
      req =>
        client.run(req).use { response =>
          if (response.status == Status.Ok)
            response.asJsonDecode[Seq[CatFact]]
          else CatRequestError(Option(response.status.reason).getOrElse("unknown")).raiseError[F, Seq[CatFact]]
        }
    )
}
