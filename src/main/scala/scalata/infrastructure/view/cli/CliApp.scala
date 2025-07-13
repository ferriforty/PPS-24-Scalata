package scalata.infrastructure.view.cli

import cats.effect.{ExitCode, IO, IOApp}
import scalata.application.services.{GameBuilder, GamePhaseService}
import scalata.infrastructure.controller.GameEngine

object CliApp extends IOApp:

  def run(args: List[String]): IO[ExitCode] =
    GameEngine().gameLoop(view = ConsoleView[IO]())
