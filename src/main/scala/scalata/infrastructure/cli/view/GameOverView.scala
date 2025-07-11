package scalata.infrastructure.cli.view

import cats.effect.Sync
import scalata.application.services.GameView
import cats.syntax.all._

object GameOverView:

  private val textToDisplay: String =
    """================ GAME OVER ================
      |
      |Do you want to go back to the main menu? y/n
      |""".stripMargin

  def gameOver[F[_]: Sync](view: GameView[F]): F[String] =
    for
      _ <- view.display(textToDisplay)
      resp <- view.getInput
    yield resp
