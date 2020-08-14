package bbc.rms.rand.catseffect

import cats.{Monad, MonadError}
import cats.effect._

object Guarantee extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val a = new Experiment[IO]().createEffect


    val effect = IO.delay(throw new IllegalArgumentException("returning error")).attempt
    effect.map(println).as(ExitCode.Success)
  }




}

class Experiment[F[_] : Sync : Monad]() {
  def createEffect: F[String] = {
    val a = Sync[F].delay("3")
    //a.map
    a
  }
}