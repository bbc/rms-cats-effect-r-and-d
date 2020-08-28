package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.RandDConfig.{HttpClientConfig, PostgreSQLConfig, RandDConfig, RedisConfig}
import cats.effect._
import cats.implicits._
import dev.profunktor.redis4cats.{Redis, RedisCommands}
import dev.profunktor.redis4cats.log4cats._
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.string.NonEmptyString
import io.chrisdavenport.log4cats.Logger
import natchez.Trace.Implicits.noop
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext
import skunk._

import scala.concurrent.duration.FiniteDuration

object AppResources {
  case class HttpServerConfig(host: NonEmptyString, port: UserPortNumber)

  final case class AppResources[F[_]](
      client: Client[F],
      psql: Resource[F, Session[F]],
      redis: RedisCommands[F, String, String]
  )

  def make[F[_]: ConcurrentEffect: ContextShift: Logger](cfg: RandDConfig): Resource[F, AppResources[F]] = {

    def mkPostgreSqlResource(c: PostgreSQLConfig): SessionPool[F] =
      Session.pooled[F](
        host = c.serverName,
        port = c.port,
        user = c.user,
        password = Some(c.password),
        database = c.serverName,
        max = c.max
      )

    def mkRedisResource(c: RedisConfig): Resource[F, RedisCommands[F, String, String]] =
      Redis[F].utf8(c.url)

    def mkHttpClient(connectTimeout: FiniteDuration, requestTimeout: FiniteDuration): Resource[F, Client[F]] =
      BlazeClientBuilder[F](ExecutionContext.global)
        .withConnectTimeout(connectTimeout)
        .withRequestTimeout(requestTimeout)
        .resource

    (
      mkHttpClient(cfg.httpClientConnectTimeout, cfg.httpClientRequestTimeout),
      mkPostgreSqlResource(cfg.postgreSQLConfig),
      mkRedisResource(cfg.redisConfig)
      ).mapN(AppResources.apply[F])
  }

}
