package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.Debug.EffectHelper
import cats.implicits._
import cats.effect.{ContextShift, ExitCode, IO, IOApp}

import scala.concurrent.ExecutionContext

object Shifting extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val a = IO("one").debug()
    val b = IO("two").debug()
    val c = IO("three").debug()

    val result = for {
    _ <- a
    _ <- contextShift.shift
    _ <- b
    _ <- ContextShift[IO].evalOn(ExecutionContext.global)(IO("moving to global execution context").map(println).debug())
    _ <- c
//    _ <- contextShift.shift
    } yield ()


//    val effectList = List(a, b, c)
//    val result = effectList.traverse(somethig => somethig)
    result.as(ExitCode.Success)
  }

}
