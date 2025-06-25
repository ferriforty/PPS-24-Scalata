package scalata.application.usecases

import scalata.application.services.PlayerFactory
import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}

class ChampSelectUseCase:
  def champSelect(
                 input: PlayerClasses
                 ): GameResult[(GameControllerState, Option[Player])] =

    GameResult.success(
      GameControllerState.GameRunning,
      Some(PlayerFactory().createPlayer(input))
    )



