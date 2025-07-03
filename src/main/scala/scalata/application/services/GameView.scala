package scalata.application.services

import cats.effect.{IO, Sync}
import cats.syntax.all._

trait GameView[F[_]]:
  def display[A](text: A): F[Unit]
  def getInput: F[String]
  def displayError[A](message: A): F[Unit]
  def clearScreen: F[Unit]
