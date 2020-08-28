package bbc.rms.rand.catseffect

import bbc.rms.rand.catseffect.InfoClient.Info

object InfoClient {
  abstract class Info()
}

trait InfoClient[F[_], MyInfo <: Info] {
  def process(): F[Seq[MyInfo]]
}
