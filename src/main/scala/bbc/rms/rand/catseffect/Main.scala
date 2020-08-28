package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.RandDConfig.RandDConfig
import cats.effect.{ExitCode, IO, IOApp, _}
import cats.implicits._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.http4s.server.blaze.BlazeServerBuilder

class Main extends IOApp {

  implicit val logger = Slf4jLogger.getLogger[IO]

  override def run(args: List[String]): IO[ExitCode] =
    RandDConfig[IO]().flatMap { cfg: RandDConfig =>
      Logger[IO].info(s"Loaded config $cfg") >>
        AppResources.make[IO](cfg).use { res =>
          for {
            clients <- HttpClients.make[IO](cfg.httpClientA, cfg.httpClientB, res.client)
          } yield ()
          //datasource1
          //datasource2
          //datasource3
          //httpclient
          //server

    }
//    config.load[IO].flatMap { cfg =>
//      Logger[IO].info(s"Loaded config $cfg") >>
//        AppResources.make[IO](cfg).use { res =>
//          for {
//            security <- Security.make[IO](cfg, res.psql, res.redis)
//            algebras <- Algebras.make[IO](res.redis, res.psql, cfg.cartExpiration)
//            clients <- HttpClients.make[IO](cfg.paymentConfig, res.client)
//            programs <- Programs.make[IO](cfg.checkoutConfig, algebras, clients)
//            api <- HttpApi.make[IO](algebras, programs, security)
//            _ <- BlazeServerBuilder[IO]
//                  .bindHttp(
//                    cfg.httpServerConfig.port.value,
//                    cfg.httpServerConfig.host.value
//                  )
//                  .withHttpApp(api.httpApp)
//                  .serve
//                  .compile
//                  .drain
//          } yield ExitCode.Success
//        }
//    }

}
