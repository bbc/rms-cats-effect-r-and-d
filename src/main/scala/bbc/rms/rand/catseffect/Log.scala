package bbc.rms.rand.catseffect

import cats.effect.{ExitCode, IO, IOApp}

object Log extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    IO.delay(println("aaa")).as(ExitCode.Success)
  }

}