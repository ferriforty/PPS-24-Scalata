package scalata.infrastructure.view.terminal.cli

import cats.effect.Sync
import cats.implicits.catsSyntaxApplyOps
import scalata.application.services.GameView

/** Console implementation of [[GameView]].
  *
  * Uses ANSI escape sequences to clear the screen and reads input via
  * `StdIn.readLine()`, which is blocking.
  *
  * @see
  *   GameView for method-level semantics
  */
final class ConsoleView[F[_]: Sync] extends GameView[F, String]:
  /** @inheritdoc */
  override def display[String](text: String): F[Unit] =
    clearScreen *> Sync[F].delay(println(text))

  /** @inheritdoc */
  override def getInput: F[String] =
    Sync[F].blocking(Option(scala.io.StdIn.readLine()).getOrElse("").trim)

  /** @inheritdoc */
  override def displayError[String](msg: String): F[Unit] =
    Sync[F].delay(println(s"Error: $msg"))

  /** @inheritdoc */
  override def clearScreen: F[Unit] =
    Sync[F].delay(print("\u001b[2J\u001b[H"))
