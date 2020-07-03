package bbc.rms.rand.catseffect

import cats.effect.Sync

import scala.util.Try
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

object RandDConfig {
  def apply[F[_]: Sync]() :F[RandDConfig] = {
    Sync[F].delay(new RandDConfig())
  }
}

class RandDConfig() {
  val config: Config = ConfigFactory.load()

  val serviceName: String = config.getString("service.name")
  val httpInterface: String = config.getString("service.http.interface")
  val httpPort: Int = config.getInt("service.http.port")

  val defaultPageLimit: Int = config.getInt("service.pagination.default-limit")
  val maximumPageLimit: Int = config.getInt("service.pagination.max-limit")

  val collectionLimit: Option[Int] = Try(Option(config.getInt("retrievals-limits.collection"))).toOption.flatten

  val defaultImagePid: String = config.getString("service.default-image.pid")

  val networkSchemaUrl: String = config.getString("service.schema.network-response-url")
  val networksSchemaUrl: String = config.getString("service.schema.networks-response-url")
}

