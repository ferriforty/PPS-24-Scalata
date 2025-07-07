package scalata.infrastructure.cli

import scalata.infrastructure.cli.controller.GameEngine
import cats.effect.{ExitCode, IO, IOApp}
import scalata.application.services.{GameBuilder, GamePhaseService}
import scalata.infrastructure.cli.view.ConsoleView

object CliApp extends IOApp:

  def run(args: List[String]): IO[ExitCode] =
    GameEngine().gameLoop(view = ConsoleView[IO]())
