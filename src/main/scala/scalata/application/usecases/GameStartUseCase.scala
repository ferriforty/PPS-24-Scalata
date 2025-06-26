package scalata.application.usecases

import scalata.application.services.WorldBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class GameStartUseCase:
  def newGame(
      input: Boolean,
      worldBuilder: WorldBuilder
  ): GameResult[(GameControllerState, WorldBuilder)] =
    if input then GameResult.success(GameControllerState.ChampSelect, worldBuilder)
    else GameResult.error(GameError.GameOver(), "game over")
