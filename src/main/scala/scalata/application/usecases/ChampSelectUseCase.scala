package scalata.application.usecases

import cats.Monad
import cats.syntax.all.*
import scalata.application.services.GameBuilder
import scalata.application.services.factories.PlayerFactory
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}

/** Use-case that **concludes champion selection** by creating the player
 * and moving the finite-state machine to the _Game-Running_ phase.
 */
class ChampSelectUseCase:

  /** Create the player and emit the next step.
   *
   * @param input        effect that yields the chosen [[PlayerClasses]] value
   * @param worldBuilder current builder that will receive the new player
   * @tparam F effect type with a Cats [[cats.Monad]] instance
   */
  def champSelect[F[_]: Monad](
      input: F[PlayerClasses],
      worldBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =

    input.map: raw =>
      val player = PlayerFactory().create(raw)

      GameResult.success(
        (GameControllerState.GameRunning, worldBuilder.withPlayer(Some(player)))
      )
