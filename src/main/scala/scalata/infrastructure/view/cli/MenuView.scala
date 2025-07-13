package scalata.infrastructure.view.cli

import cats.effect.Sync
import cats.syntax.all.*
import scalata.application.services.GameView

object MenuView:

  private val textToDisplay: String =
    """ -> Welcome to Scalata <-
      | --> This Menu is useless <--
      | --> Do you want to play? <--
      | ---> [y/n] <---""".stripMargin

  def menu[F[_]: Sync](view: GameView[F]): F[String] =
    for
      _ <- view.display(textToDisplay)
      resp <- view.getInput
    yield resp
