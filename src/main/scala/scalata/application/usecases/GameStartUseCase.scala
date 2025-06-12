package scalata.application.usecases

import scalata.domain.util.{GameControllerState, GameResult, GameError}

class GameStartUseCase:
  def newGame(input: Boolean): GameResult[GameControllerState] =
    if input then
      GameResult.success(GameControllerState.ChampSelect)
    else
      GameResult.error(GameError.GameOver(), "game over")
