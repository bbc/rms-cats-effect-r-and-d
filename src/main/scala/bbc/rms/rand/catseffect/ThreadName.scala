package bbc.rms.rand.catseffect

import cats._
import cats.effect._

/**
 * from https://github.com/inner-product/essential-effects-code/tree/solutions/exercises/src/main/scala/com.innerproduct.ee
 */

case class ThreadName(name: String) extends AnyVal // <1>

object ThreadName {
  def current[F[_]: Sync](): F[ThreadName] = // <2>
    Sync[F].delay(ThreadName(Thread.currentThread().getName)) // <3>

  implicit val show: Show[ThreadName] =
    Show.show(tn => Colorize.reversed(tn.name)) // <4>
}