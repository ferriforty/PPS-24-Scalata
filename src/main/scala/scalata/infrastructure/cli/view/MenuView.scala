package scalata.infrastructure.cli.view

import cats.effect.Sync
import scalata.application.services.GameView
import cats.syntax.all._

object MenuView:

  private val textToDisplay: String =
    """ -> Welcome to Scalata <-
      | --> This Menu is useless <--
      | --> Do you want to play? <--
      | ---> [y/n] <---""".stripMargin

  def menu[F[_]: Sync](view: ConsoleView[F]): F[String] =
    for
      _    <- view.display(textToDisplay)
      resp <- view.getInput
    yield resp