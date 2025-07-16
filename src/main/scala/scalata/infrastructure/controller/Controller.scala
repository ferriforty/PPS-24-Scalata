package scalata.infrastructure.controller

import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameResult}

/** Base controller interface.
  *
  * <ul> <li><b>start</b> – executes one screen / state step and returns a
  * <code>GameResult</code> carrying the next <code>(GameControllerState,
  * GameBuilder)</code>.</li> </ul>
  */
trait Controller[F[_]]:
  def start(
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]]
