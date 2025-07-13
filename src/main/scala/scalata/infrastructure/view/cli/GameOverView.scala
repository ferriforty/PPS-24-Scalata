package scalata.infrastructure.view.cli

import cats.effect.Sync
import cats.syntax.all.*
import scalata.application.services.GameView

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
