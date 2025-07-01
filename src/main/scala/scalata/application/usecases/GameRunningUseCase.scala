package scalata.application.usecases

import scalata.domain.util.{GameError, GameResult, PlayerCommand}
import scalata.domain.world.GameSession

class GameRunningUseCase:

  final def execTurn(gameSession: GameSession, command: Option[PlayerCommand]): GameResult[GameSession] =
    command.fold(GameResult.success(gameSession)):
      case PlayerCommand.Movement(direction) => ???
      case PlayerCommand.Attack(direction) => ???
      case PlayerCommand.Use(itemName) => ???
      case PlayerCommand.Interact(direction) => ???
      case PlayerCommand.Quit => GameResult.error(GameError.GameOver(), "GameOver")

