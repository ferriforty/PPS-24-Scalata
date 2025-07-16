package scalata.infrastructure.controller

import cats.effect.Sync
import scalata.application.services.GameBuilder
import scalata.application.usecases.GameStartUseCase
import scalata.domain.util.{GameControllerState, GameResult}

/** Controller for the **main menu**.
 *
 * <ul>
 * <li>Calls [[GameStartUseCase]] to ask the player if a new run should begin.</li>
 * <li>Returns the next [[GameControllerState]] together with the updated
 * [[GameBuilder]].</li>
 * </ul>
 *
 * @param askStart effect that yields <b>true</b> to start or <b>false</b> to quit
 */
class MenuController[F[_]: Sync](askStart: F[Boolean]) extends Controller[F]:
  override def start(
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =

    GameStartUseCase().newGame(askStart, gameBuilder)
