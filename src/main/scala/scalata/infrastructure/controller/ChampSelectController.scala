package scalata.infrastructure.controller

import cats.effect.Sync
import scalata.application.services.GameBuilder
import scalata.application.usecases.ChampSelectUseCase
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}

/** Controller for the **champion-selection** screen.
 *
 * <ul>
 * <li>Delegates to [[ChampSelectUseCase]] to obtain the player’s chosen class.</li>
 * <li>Propagates the resulting <code>(nextPhase, GameBuilder)</code> pair.</li>
 * </ul>
 *
 * @param askClass effect that yields the selected [[PlayerClasses]]
 */
class ChampSelectController[F[_]: Sync](
    askClass: F[PlayerClasses]
) extends Controller[F]:

  override def start(
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =

    ChampSelectUseCase().champSelect(
      askClass,
      gameBuilder
    )
