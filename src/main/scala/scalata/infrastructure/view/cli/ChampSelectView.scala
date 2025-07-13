package scalata.infrastructure.view.cli

import cats.effect.Sync
import cats.syntax.all.*
import scalata.application.services.GameView

object ChampSelectView:

  private val textToDisplay: String =
    """ -> Let 's create your champion now <-
      | --> What is your play style? <--
      | ---> [M]age(ranged) <---
      | ---> [B]arbarian(melee) <---
      | ---> [A]ssassin(stealth) <---""".stripMargin

  def champSelect[F[_]: Sync](view: GameView[F]): F[String] =
    for
      _ <- view.display(textToDisplay)
      resp <- view.getInput
    yield resp
  