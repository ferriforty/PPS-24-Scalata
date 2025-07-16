package scalata.infrastructure.view.terminal

import cats.effect.Sync
import cats.syntax.all.*
import scalata.application.services.GameView
import scalata.domain.util.GameControllerState
import scalata.infrastructure.controller.*

/** Terminal-view helper utilities shared.
  *
  * Responsibilities <br> • Wire a map `GameControllerState ⇒ Controller[F]` so
  * that the [[scalata.infrastructure.controller.GameEngine GameEngine]]<br> can
  * route the state machine to the correct controller. • Provide a generic `run`
  * loop that repeatedly asks the user for input until the supplied `parse`
  * function succeeds. <br> • Offer small helper parsers (e.g. `booleanParse`)
  * used by several interactive screens.
  */
object Shared:

  /** Build the *state → controller* lookup table for the terminal UI.
    *
    * @param view
    *   shared [[scalata.application.services.GameView GameView]] used by all
    *   controllers for rendering and input
    * @tparam F
    *   effect type (e.g. `IO`) in which controllers operate
    * @return
    *   a partial function: given the current
    *   [[scalata.domain.util.GameControllerState]], return the corresponding
    *   controller instance
    */
  def getControllersMap[F[_]: Sync](
      view: GameView[F, String]
  ): GameControllerState => Controller[F] =
    Map(
      GameControllerState.Menu -> MenuController[F](
        MenuView[F](view).ask
      ),
      GameControllerState.ChampSelect -> ChampSelectController[F](
        ChampSelectView[F](view).ask
      ),
      GameControllerState.GameRunning -> GameController[F](
        GameRunView[F](view).ask
      ),
      GameControllerState.GameOver -> GameOverController[F](
        GameOverView[F](view).ask
      )
    )

  /** Generic input loop that keeps asking the player until `parse` returns
    * `Some`.
    *
    * @param gameView
    *   view through which we display prompts / errors and read input
    * @param parse
    *   function that tries to convert raw input `I` to domain value `O`
    * @return
    *   the successfully parsed value wrapped in the effect `F`
    */
  def run[F[_]: Sync, I, O](
      gameView: GameView[F, I],
      parse: I => Option[O]
  ): F[O] =

    gameView.getInput.flatMap: raw =>
      parse(raw) match
        case Some(out) => Sync[F].pure(out)
        case None => gameView.displayError("Try again!") *> run(gameView, parse)

  /** Parse a yes/no question.
    *
    * Accepts only a single-letter **y** or **n** (case-insensitive), trimmed of
    * surrounding white-space.
    *
    * @param raw
    *   user-supplied string
    * @return
    *   Some(true) if `"y"` Some(false) if `"n"` None otherwise
    */
  def booleanParse(raw: String): Option[Boolean] =
    raw.toLowerCase.split("\\s+").toList match
      case "y" :: Nil => Some(true)
      case "n" :: Nil => Some(false)
      case _          => None
