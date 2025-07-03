package scalata.infrastructure.cli.view

import cats.effect.IO
import scalata.application.services.GameView

final class TestView(input: String) extends GameView[IO]:
  def display[A](text: A): IO[Unit] = IO.unit
  def getInput: IO[String] = IO.pure(input)
  def displayError[A](message: A): IO[Unit] = IO.unit
  def clearScreen: IO[Unit] = IO.unit
