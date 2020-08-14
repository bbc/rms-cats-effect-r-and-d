package bbc.rms.rand.catseffect

import cats.{Monad, MonadError}
import cats.effect._
import cats.implicits._

object Guarantee extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
//    val a = new Experiment[IO]().createEffect
    val effect = IO.delay(throw new IllegalArgumentException("returning error")).attempt
    effect.map(println).as(ExitCode.Success)
  }




}

class Experiment[F[_]: Sync]() {
  def createEffect: F[String] = Sync[F].delay("3")
}