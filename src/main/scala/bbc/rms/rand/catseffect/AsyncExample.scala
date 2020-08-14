package bbc.rms.rand.catseffect

import java.util.concurrent.CompletableFuture

import cats.{Monad, MonadError}
import cats.effect._
import cats.implicits._

import scala.jdk.FunctionConverters._

object AsyncExample extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    createEffect(IO(produceJavaCompletableFuture())).map(println).as(ExitCode.Success)

  def createEffect[F[_]: Async, A](cfa: F[CompletableFuture[A]]): F[A] =
    Async[F].flatMap(cfa) { futureA =>
      Async[F].async { cb =>
        val handler: (A, Throwable) => Unit = {
          case (a, null) => cb(Right(a))
          case (null, t) => cb(Left(t))
          case (a, t) => sys.error("completable future should always have one null")
        }
        futureA.handle[Unit](handler.asJavaBiFunction)
        ()
      }
    }

  def produceJavaCompletableFuture(): CompletableFuture[String] =
    CompletableFuture.completedFuture("java future triggered")
}