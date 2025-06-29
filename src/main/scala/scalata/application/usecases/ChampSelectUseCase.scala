package scalata.application.usecases

import scalata.application.services.{PlayerFactory, GameBuilder}
import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}

class ChampSelectUseCase:
  def champSelect(
      input: PlayerClasses,
      worldBuilder: GameBuilder
  ): GameResult[(GameControllerState, GameBuilder)] =

    val player = PlayerFactory().createPlayer(input) match
      case GameResult.Success(p, _)     => p
      case GameResult.Error(_, message) =>
        throw new IllegalStateException(message)

    GameResult.success(
      GameControllerState.GameRunning,
      worldBuilder.withPlayer(Some(player))
    )
