package bbc.rms.rand.catseffect

import cats.effect._
import cats.implicits._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import org.http4s.server.blaze.BlazeServerBuilder
import cats.effect.{ ExitCode, IO, IOApp }

class Main extends IOApp {

  implicit val logger = Slf4jLogger.getLogger[IO]

  override def run(args: List[String]): IO[ExitCode] = ???

}
