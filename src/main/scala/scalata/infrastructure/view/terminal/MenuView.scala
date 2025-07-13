package scalata.infrastructure.view.terminal

import cats.effect.Sync
import scalata.application.services.GameView
import scalata.infrastructure.view.View

class MenuView[F[_]: Sync, I](
    val view: GameView[F, I]
) extends View[MenuView[F, I], String, Boolean, I, F](view):

  override protected def banner: String =
    """ -> Welcome to Scalata <-
      | --> This Menu is useless <--
      | --> Do you want to play? <--
      | ---> [y/n] <---""".stripMargin

  override protected def parse(raw: I): Option[Boolean] =
    Shared.booleanParse[I](raw)
