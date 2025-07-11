package scalata.infrastructure.terminal.cli

import cats.effect.{ExitCode, IO, IOApp}
import scalata.application.services.{GameBuilder, GamePhaseService}
import scalata.infrastructure.terminal.cli.controller.GameEngine
import scalata.infrastructure.terminal.cli.view.ConsoleView

object CliApp extends IOApp:

  def run(args: List[String]): IO[ExitCode] =
    GameEngine().gameLoop(view = ConsoleView[IO]())
