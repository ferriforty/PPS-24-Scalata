package scalata.application.usecases.playerusecases

import scalata.application.usecases.PlayerUseCase
import scalata.domain.util.Direction
import scalata.domain.world.GameSession

class PlayerMovementUseCase extends PlayerUseCase[PlayerMovementUseCase, GameSession, Direction]:
  override def execute(param: Direction, gameSession: GameSession): GameSession =
    val world = gameSession.getWorld
    val gameState = gameSession.getGameState
    val currentRoom = world
      .getRoom(gameState.currentRoom)
      .getOrElse(
        throw new IllegalStateException(
          "Room Not defined in navigation controller"
        )
      )
    val newPos = world.player.position.moveBy(param.pointsTo)

    if currentRoom.isInside(newPos) &&
      currentRoom.getEnemyAtPosition(newPos).isEmpty &&
      currentRoom.getItemAtPosition(newPos).isEmpty then

      gameSession.updateWorld(world.updatePlayer(world.player.move(newPos)))
    gameSession
