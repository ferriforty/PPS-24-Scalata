package scalata.application.usecases

import scalata.application.services.PlayerFactory
import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}

class ChampSelectUseCase:
  def champSelect(
                 input: PlayerClasses
                 ): GameResult[(GameControllerState, Option[Player])] =

    val player = PlayerFactory().createPlayer(input) match
      case GameResult.Success(p, _) => p
      case GameResult.Error(_, message) => throw new IllegalStateException(message)

    GameResult.success(
      GameControllerState.GameRunning,
      Some(player)
    )
