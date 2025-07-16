package scalata.application.usecases

import cats.Monad
import cats.syntax.all.*
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}

/** Use-case that handles the “Start Game?” choice shown in the main menu.
 *
 * <p>
 * • **true**  → transition to <code>GameControllerState.ChampSelect</code>
 * • **false** → return a <code>GameError.GameOver</code>
 * </p>
 */
class GameStartUseCase:

  /** Evaluate the player’s answer and emit the next finite-state-machine step.
   *
   * @param input        side-effecting Boolean (<b>true</b>=start, <b>false</b>=quit)
   * @param worldBuilder builder carrying the current difficulty / seed setup
   * @tparam F effect type with a [[cats.Monad]] instance (e.g. <code>IO</code>)
   */
  def newGame[F[_]: Monad](
      input: F[Boolean],
      worldBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =

    input.map: raw =>
      if raw then
        GameResult.success((GameControllerState.ChampSelect, worldBuilder))
      else GameResult.error(GameError.GameOver())
