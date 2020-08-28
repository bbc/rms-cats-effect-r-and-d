package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.RandDConfig.{ HttpClientConfig, PostgreSQLConfig }
import cats.effect.Sync
import io.estatico.newtype.macros.newtype
import com.typesafe.config.{ Config, ConfigFactory }
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.numeric.PosInt
import org.http4s.implicits.http4sLiteralsSyntax
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString

import scala.concurrent.duration.FiniteDuration
import scala.util.Try
import scala.concurrent.duration._

object RandDConfig {
  def apply[F[_]: Sync](): F[RandDConfig] =
    Sync[F].delay(new RandDConfig())

  case class HttpClientConfig(
      protocol: String,
      host: String,
      port: Option[Int],
      connectTimeout: Option[FiniteDuration]
  ) {

    /**
      * e.g. baseUrl.withPath("/networks")
      */
    val baseUrl = uri"$protocol://$host:$port"
  }

  case class PostgreSQLConfig(
      dataSourceClass: String,
      serverName: String,
      databaseName: String,
      port: Int,
      user: String,
      password: String,
      max: Int
  )

  case class RedisConfig(url: String)

  class RandDConfig() {
    val config: Config = ConfigFactory.load()

    val serviceName: String = config.getString("service.name")

    val httpClientRequestTimeout = config.getLong("client.request-timeout-millis").milliseconds
    val httpClientConnectTimeout = config.getLong("client.connect-timeout-millis").milliseconds

    val catFactHttpConfig = HttpClientConfig(
      config.getString("client.catfact-client.protocol"),
      config.getString("client.catfact-client.host"),
      Try(config.getInt("client.catfact-client.port")).toOption,
      Try(config.getLong("client.catfact-client.connect-timeout-millis").millisecond).toOption
    )

    val btcInfoHttpConfig = HttpClientConfig(
      config.getString("client.btc-client.protocol"),
      config.getString("client.btc-client.host"),
      Try(config.getInt("client.btc-client.port")).toOption,
      Try(config.getLong("client.btc-client.connect-timeout-millis").millisecond).toOption
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

    val networkSchemaUrl: String  = config.getString("service.schema.network-response-url")
    val networksSchemaUrl: String = config.getString("service.schema.networks-response-url")
  }

}
