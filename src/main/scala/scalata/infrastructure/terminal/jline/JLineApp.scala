package scalata.infrastructure.terminal.jline

import cats.effect.{ExitCode, IO, IOApp}
import scalata.infrastructure.terminal.jline.view.JLineView
import scalata.infrastructure.terminal.cli.controller.GameEngine

object JLineApp extends IOApp:

  def run(args: List[String]): IO[ExitCode] =
    JLineView[IO]().use: view =>
      GameEngine().gameLoop(view = view)
