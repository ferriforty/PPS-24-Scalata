package scalata.infrastructure.view.terminal

import cats.effect.Sync
import scalata.application.services.GameView
import scalata.infrastructure.view.View

class GameOverView[F[_]: Sync](
    val view: GameView[F, String]
) extends View[GameOverView[F], String, Boolean, String, F](view):

  override protected def banner: String =
    """================ GAME OVER ================
      |
      |Do you want to go back to the main menu? y/n
      |""".stripMargin

  override protected def parse(raw: String): Option[Boolean] =
    Shared.booleanParse(raw)
