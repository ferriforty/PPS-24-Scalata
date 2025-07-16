package scalata.infrastructure.view.terminal.cli

import cats.effect.{ExitCode, IO, IOApp}
import scalata.domain.util.GameControllerState
import scalata.infrastructure.controller.GameEngine
import scalata.infrastructure.view.terminal.Shared

/** Command-line entry-point of *Scalata*’s **terminal UI** using Standard I/O.
  *
  * @return
  *   an [[cats.effect.ExitCode]]: • `ExitCode.Success` (0) on graceful
  *   termination; • `ExitCode.Error` (1) on unrecoverable failure.
  */
object CliApp extends IOApp:

  private val view = ConsoleView[IO]()

  def run(args: List[String]): IO[ExitCode] =
    GameEngine[IO]()
      .gameLoop(controllers = Shared.getControllersMap[IO](view))
