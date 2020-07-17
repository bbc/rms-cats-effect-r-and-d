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

object AppResources {
  case class HttpServerConfig(host: NonEmptyString, port: UserPortNumber)

  final case class AppResources[F[_]](  client: Client[F],
                                        psql: Resource[F, Session[F]],
                                        redis: RedisCommands[F, String, String] )

  def make[F[_]: ConcurrentEffect: ContextShift: Logger](cfg: RandDConfig): Resource[F, AppResources[F]] = {

    def mkPostgreSqlResource(c: PostgreSQLConfig): SessionPool[F] =
      Session.pooled[F](
          host = c.serverName.value,
          port = c.port.value,
          user = c.user.value,
          password = Some(c.password.value),
          database = c.serverName.value,
          max = c.max.value
        )

    def mkRedisResource(c: RedisConfig): Resource[F, RedisCommands[F, String, String]] =
      Redis[F].utf8(c.url.value)

    def mkHttpClient(c: HttpClientConfig): Resource[F, Client[F]] =
      c.connectTimeout.foldLeft(
        BlazeClientBuilder[F](ExecutionContext.global)
          .withRequestTimeout(c.requestTimeout)
      )((bcBuilder, connectTimeout) => bcBuilder.withConnectTimeout(connectTimeout)).resource

    (mkHttpClient(cfg.httpClient), mkPostgreSqlResource(cfg.postgreSQLConfig), mkRedisResource(cfg.redisConfig))
      .mapN(AppResources.apply[F])
  }

}
