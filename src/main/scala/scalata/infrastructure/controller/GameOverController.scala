package scalata.infrastructure.controller

import cats.effect.Sync
import scalata.application.services.GameBuilder
import scalata.application.usecases.GameOverUseCase
import scalata.domain.util.{GameControllerState, GameResult}

/** Controller for the <code>GameOver</code> screen.
  *
  * <ul> <li>Invokes [[GameOverUseCase]] to ask the player whether to
  * restart.</li> <li>Returns either <code>(GameControllerState.Menu,
  * newBuilder)</code> when the player chooses to restart, or propagates the
  * quit signal.</li> </ul>
  *
  * @param askRestart
  *   side-effecting Boolean (<b>true</b>=restart, <b>false</b>=quit)
  */
class GameOverController[F[_]: Sync](
    askRestart: F[Boolean]
) extends Controller[F]:
  override def start(
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =
    GameOverUseCase().gameOver(
      askRestart,
      gameBuilder
    )
