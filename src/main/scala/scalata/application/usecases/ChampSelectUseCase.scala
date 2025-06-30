package scalata.application.usecases

import scalata.application.services.GameBuilder
import scalata.application.services.factories.PlayerFactory
import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}

class ChampSelectUseCase:
  def champSelect(
      input: PlayerClasses,
      worldBuilder: GameBuilder
  ): GameResult[(GameControllerState, GameBuilder)] =

    val player = PlayerFactory().create(input)

    GameResult.success(
      GameControllerState.GameRunning,
      worldBuilder.withPlayer(Some(player))
    )
