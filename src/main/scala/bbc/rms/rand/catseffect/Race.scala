package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.Debug.EffectHelper
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Race extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val a = IO(Thread.sleep(1000), "one")
    val b = IO("two")

    val c = IO.racePair(a, b).flatMap {
      case Left((aResult, bFiber)) =>
        bFiber.cancel *> IO.delay(println("b is not needed")).map(_ => Left(aResult))
      case Right((aFiber, bResult)) =>
        aFiber.cancel *> IO.delay(println("a is not needed")).map(_ => Right(bResult))
    }

    c.map(println).as(ExitCode.Success)
  }

}