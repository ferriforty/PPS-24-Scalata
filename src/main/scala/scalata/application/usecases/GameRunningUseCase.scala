package scalata.application.usecases

import cats.Monad
import cats.syntax.all.*
import scalata.application.usecases.enemyusecases.{
  EnemyAttackUseCase,
  EnemyMovementUseCase
}
import scalata.application.usecases.playerusecases.{
  PlayerAttackUseCase,
  PlayerInteractUseCase,
  PlayerInventoryUseCase,
  PlayerMovementUseCase
}
import scalata.domain.util.{GameError, GameResult, PlayerCommand}
import scalata.domain.world.GameSession

class GameRunningUseCase:

  final def execTurn[F[_]: Monad](
      gameSession: GameSession,
      command: F[Option[PlayerCommand]]
  ): F[GameResult[GameSession]] =

    command.map: raw =>
      val turn: GameResult[GameSession] =
        raw.fold(GameResult.error(GameError.InvalidInput(raw.toString))):
          case PlayerCommand.Movement(direction) =>
            PlayerMovementUseCase().execute(direction, gameSession)
          case PlayerCommand.Attack(direction) =>
            PlayerAttackUseCase().execute(direction, gameSession)
          case PlayerCommand.Use(itemName) =>
            PlayerInventoryUseCase().execute(itemName, gameSession)
          case PlayerCommand.Interact(direction) =>
            PlayerInteractUseCase().execute(direction, gameSession)
          case PlayerCommand.Quit => GameResult.error(GameError.GameOver())
          case PlayerCommand.Help => GameResult.error(GameError.Help())

      turn match
        case GameResult.Success(gs, _) =>
          val currentRoom = gs.getWorld
            .getRoom(gs.getGameState.currentRoom)
            .getOrElse(
              throw IllegalStateException(
                "Room Not defined in Interact use case"
              )
            )

          GameResult.success(
            gs.updateWorld(
              gs.getWorld
                .updatePlayer(
                  EnemyAttackUseCase().execute(currentRoom, gs)
                )
                .updateRoom(
                  EnemyMovementUseCase().execute(currentRoom, gs)
                )
            )
          )
        case _ => turn
