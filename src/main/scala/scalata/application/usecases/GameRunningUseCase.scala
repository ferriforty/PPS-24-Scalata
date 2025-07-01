package scalata.application.usecases

import scalata.application.usecases.playerusecases.{PlayerAttackUseCase, PlayerInteractUseCase, PlayerInventoryUseCase, PlayerMovementUseCase}
import scalata.domain.util.{GameError, GameResult, PlayerCommand}
import scalata.domain.world.GameSession

class GameRunningUseCase:

  final def execTurn(gameSession: GameSession, command: Option[PlayerCommand]): GameResult[GameSession] =
    command.fold(GameResult.success(gameSession)):
      case PlayerCommand.Movement(direction) => PlayerMovementUseCase().execute(direction, gameSession)
      case PlayerCommand.Attack(direction) => PlayerAttackUseCase().execute(direction, gameSession)
      case PlayerCommand.Use(itemName) => PlayerInventoryUseCase().execute(itemName, gameSession)
      case PlayerCommand.Interact(direction) => PlayerInteractUseCase().execute(direction, gameSession)
      case PlayerCommand.Quit => GameResult.error(GameError.GameOver(), "GameOver")

