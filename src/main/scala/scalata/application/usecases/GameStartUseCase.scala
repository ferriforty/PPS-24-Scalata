package scalata.application.usecases

import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class GameStartUseCase:
  def newGame(
      input: Boolean,
      worldBuilder: GameBuilder
  ): GameResult[(GameControllerState, GameBuilder)] =
    if input then
      GameResult.success(GameControllerState.ChampSelect, worldBuilder)
    else GameResult.error(GameError.GameOver(), "game over")
