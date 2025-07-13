package scalata.infrastructure.view.terminal

import cats.effect.Sync
import scalata.application.services.GameView
import scalata.infrastructure.view.View

class GameOverView[F[_]: Sync, I](
    val view: GameView[F, I]
) extends View[GameOverView[F, I], String, Boolean, I, F](view):

  override protected def banner: String =
    """================ GAME OVER ================
      |
      |Do you want to go back to the main menu? y/n
      |""".stripMargin

  override protected def parse(raw: I): Option[Boolean] =
    Shared.booleanParse[I](raw)
