package scalata.infrastructure.view.terminal

import cats.effect.Sync
import scalata.application.services.GameView
import scalata.infrastructure.view.View

class MenuView[F[_]: Sync](
    val view: GameView[F, String]
) extends View[MenuView[F], String, Boolean, String, F](view):

  override protected def banner: String =
    """ -> Welcome to Scalata <-
      | --> This Menu is useless <--
      | --> Do you want to play? <--
      | ---> [y/n] <---""".stripMargin

  override protected def parse(raw: String): Option[Boolean] =
    Shared.booleanParse(raw)
