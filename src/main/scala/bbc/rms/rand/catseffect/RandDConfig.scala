package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.RandDConfig.{HttpClientConfig, PostgreSQLConfig}
import cats.effect.Sync
import com.typesafe.config.{Config, ConfigFactory}
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.numeric.PosInt
import org.http4s.implicits.http4sLiteralsSyntax
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString

import scala.concurrent.duration.FiniteDuration
import scala.util.Try
import scala.concurrent.duration._

object RandDConfig {
  def apply[F[_]: Sync]() :F[RandDConfig] = {
    Sync[F].delay(new RandDConfig())
  }

  case class HttpClientConfig( protocol: NonEmptyString,
                               host: NonEmptyString,
                               port: UserPortNumber,
                               requestTimeout: FiniteDuration,
                               connectTimeout: Option[FiniteDuration]) {
    /**
     * e.g. baseUrl.withPath("/networks")
     */
    val baseUrl = uri"$protocol://$host:$port"
  }

  case class PostgreSQLConfig( dataSourceClass: NonEmptyString,
                               serverName: NonEmptyString,
                               databaseName: NonEmptyString,
                               port: UserPortNumber,
                               user: NonEmptyString,
                               password: NonEmptyString,
                               max: PosInt )

  case class RedisConfig( url: NonEmptyString )

class RandDConfig() {
  val config: Config = ConfigFactory.load()

  val serviceName: String = config.getString("service.name")

  val httpClient = HttpClientConfig(
    config.getString("client.a-client.protocol"),
    config.getString("client.a-client.host"),
    config.getInt("client.a-client.port"),
    config.getLong("client.a-client.request-timeout-millis").milliseconds,
    Try(config.getLong("client.a-client.connect-timeout-millis").millisecond).toOption
  )

  val postgreSQLConfig = PostgreSQLConfig(
    config.getString("database.dataSourceClass"),
    config.getString("database.properties.serverName"),
    config.getString("database.properties.databaseName"),
    config.getInt("database.properties.portNumber"),
    config.getString("database.properties.user"),
    config.getString("database.properties.password"),
    config.getInt("database.numThreads")
  )

  val redisConfig = RedisConfig(
    config.getString("redis.url")
  )

  val defaultPageLimit: Int = config.getInt("service.pagination.default-limit")
  val maximumPageLimit: Int = config.getInt("service.pagination.max-limit")

  val collectionLimit: Option[Int] = Try(Option(config.getInt("retrievals-limits.collection"))).toOption.flatten

  val defaultImagePid: String = config.getString("service.default-image.pid")

  val networkSchemaUrl: String = config.getString("service.schema.network-response-url")
  val networksSchemaUrl: String = config.getString("service.schema.networks-response-url")
}

