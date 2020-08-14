package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.Debug.EffectHelper
import cats.implicits._
import cats.effect.{ ContextShift, ExitCode, IO, IOApp }

object Shifting extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val a = IO("one").debug()
    val b = IO("two").debug()
    val c = IO("three").debug()

    val effectList = List(a, b, c)
    val result = effectList.traverse(somethig => somethig)
    result.as(ExitCode.Success)
  }

}
