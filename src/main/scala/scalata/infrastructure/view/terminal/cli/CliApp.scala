package scalata.infrastructure.view.terminal.cli

import cats.effect.{ExitCode, IO, IOApp}
import scalata.domain.util.GameControllerState
import scalata.infrastructure.controller.GameEngine
import scalata.infrastructure.view.terminal.Shared

object CliApp extends IOApp:

  private val view = ConsoleView[IO]()

  def run(args: List[String]): IO[ExitCode] =
    GameEngine[IO, String]()
      .gameLoop(controllers = Shared.getControllersMap[IO, String](view))
