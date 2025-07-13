package scalata.infrastructure.view.jline

import cats.effect.{ExitCode, IO, IOApp}
import scalata.infrastructure.controller.GameEngine

object JLineApp extends IOApp:

  def run(args: List[String]): IO[ExitCode] =
    JLineView[IO]().use: view =>
      GameEngine().gameLoop(view = view)
