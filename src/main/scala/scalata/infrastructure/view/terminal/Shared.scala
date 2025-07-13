package scalata.infrastructure.view.terminal

import cats.effect.Sync
import cats.syntax.all.*
import scalata.application.services.GameView
import scalata.domain.util.GameControllerState
import scalata.infrastructure.controller.*

object Shared:
  def getControllersMap[F[_] : Sync, I](view: GameView[F, I]): GameControllerState => Controller[F] =
    Map(
      GameControllerState.Menu -> MenuController[F, I](MenuView[F, I](view).ask),
      GameControllerState.ChampSelect -> ChampSelectController[F, I](ChampSelectView[F, I](view).ask),
      GameControllerState.GameRunning -> GameController[F, I](GameRunView[F, I](view).ask),
      GameControllerState.GameOver -> GameOverController[F, I](GameOverView[F, I](view).ask)
    )

  def run[F[_] : Sync, I, O](
                              gameView: GameView[F, I],
                              parse: I => Option[O]
                            ): F[O] =

    gameView.getInput.flatMap: raw =>
      parse(raw) match
        case Some(out) => Sync[F].pure(out)
        case None => gameView.displayError("Try again!") *> run(gameView, parse)

  def booleanParse[I](raw: I): Option[Boolean] =
    raw.toString.toLowerCase.split("\\s+").toList match
      case "y" :: Nil => Some(true)
      case "n" :: Nil => Some(false)
      case _ => None


