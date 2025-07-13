package scalata.application.services

import cats.syntax.all.*

trait GameView[F[_], I]:
  def display[A](text: A): F[Unit]

  def getInput: F[I]

  def displayError[A](message: A): F[Unit]

  def clearScreen: F[Unit]
