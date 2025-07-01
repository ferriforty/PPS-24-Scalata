package scalata.application.usecases.playerusecases

import scalata.application.usecases.PlayerUseCase
import scalata.domain.util.{Direction, GameError, GameResult}
import scalata.domain.world.GameSession

class PlayerMovementUseCase extends PlayerUseCase[PlayerMovementUseCase, GameResult[GameSession], Direction]:
  override def execute(param: Direction, gameSession: GameSession): GameResult[GameSession] =
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

      GameResult.success(gameSession.updateWorld(world.updatePlayer(world.player.move(newPos))))
    else if currentRoom.exits.contains(param) then
      val neighbor = world
        .getNeighbor(param, currentRoom.id)
        .getOrElse(
          throw new IllegalStateException("Room" + currentRoom.id + "must have neighbor at " + param)
        )

      val entrance = neighbor
        .getDoorPosition(param.getOpposite)
        .moveBy(param.pointsTo)

      GameResult.success(
        gameSession
          .updateGameState(gameState.setRoom(neighbor.id))
          .updateWorld(world
            .updateRoom(
              neighbor.withEnemies(
                neighbor.enemies.map(e =>
                  if e.position == entrance then
                    e.move(e.position.moveBy(param.pointsTo))
                  else e
                )
              )
            )
            .updatePlayer(world.player.move(entrance))
          )
      )

    else GameResult.error(GameError.InvalidInput(param.toString), "input not valid")
