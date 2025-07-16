package scalata.application.usecases

import cats.Monad
import cats.syntax.all.*
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}

/** Handles the <i>Game Over</i> prompt (“Restart?”). */
class GameOverUseCase:

  /** Evaluate the player’s choice and emit the next step.
    *
    * @param input
    *   effect that yields <code>true</code> to restart or <code>false</code> to
    *   quit
    * @param gameBuilder
    *   builder carried forward when the game restarts
    * @tparam F
    *   effect type with a <code>cats.Monad</code> instance
    */
  def gameOver[F[_]: Monad](
      input: F[Boolean],
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =

    input.map: raw =>
      if raw then GameResult.success((GameControllerState.Menu, gameBuilder))
      else GameResult.error(GameError.GameOver())
