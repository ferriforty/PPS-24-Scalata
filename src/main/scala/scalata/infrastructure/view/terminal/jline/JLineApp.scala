package scalata.infrastructure.view.terminal.jline

import cats.effect.{ExitCode, IO, IOApp}
import scalata.domain.util.GameControllerState
import scalata.infrastructure.controller.GameEngine
import scalata.infrastructure.view.terminal.Shared


object JLineApp extends IOApp:

  private val view = JLineView[IO]()

  def run(args: List[String]): IO[ExitCode] =
    view.use: view =>
      GameEngine[IO, String]().gameLoop(controllers = Shared.getControllersMap[IO, String](view))
