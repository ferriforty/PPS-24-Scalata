package scalata.infrastructure.view.terminal.jline

import cats.effect.{ExitCode, IO, IOApp}
import scalata.domain.util.GameControllerState
import scalata.infrastructure.controller.GameEngine
import scalata.infrastructure.view.terminal.Shared

/** Command-line entry-point of *Scalata*’s **terminal UI** using JLine as I/O.
 *
 * @return an [[cats.effect.ExitCode]]:
 *         • `ExitCode.Success` (0) on graceful termination;
 *         • `ExitCode.Error`   (1) on unrecoverable failure.
 */
object JLineApp extends IOApp:

  private val view = JLineView[IO]()

  def run(args: List[String]): IO[ExitCode] =
    view.use: view =>
      GameEngine[IO]()
        .gameLoop(controllers = Shared.getControllersMap[IO](view))
