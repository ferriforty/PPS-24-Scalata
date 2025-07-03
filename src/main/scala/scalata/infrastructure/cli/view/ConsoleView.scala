package scalata.infrastructure.cli.view

import cats.effect.Sync
import cats.implicits.catsSyntaxApplyOps
import scalata.application.services.GameView

final class ConsoleView[F[_]: Sync] extends GameView[F]:
  override def display[String](text: String): F[Unit] =
    clearScreen *> Sync[F].delay(println(text))

  override def getInput: F[String] =
    Sync[F].blocking(scala.io.StdIn.readLine().trim)

  override def displayError[String](msg: String): F[Unit] =
    Sync[F].delay(println(s"Error: $msg"))

  override def clearScreen: F[Unit] =
    Sync[F].delay(print("\u001b[2J\u001b[H"))
