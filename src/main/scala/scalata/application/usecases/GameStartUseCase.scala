package scalata.application.usecases

import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class GameStartUseCase:
  def newGame(
      input: Boolean
  ): GameResult[(GameControllerState, Option[Player])] =
    if input then GameResult.success(GameControllerState.ChampSelect, None)
    else GameResult.error(GameError.GameOver(), "game over")
