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
          "Room Not defined in movement use case"
        )
      )
    val newPos = world.player.position.moveBy(param.vector)

    if currentRoom.isInside(newPos) &&
      currentRoom.getAliveEnemyAtPosition(newPos).isEmpty &&
      currentRoom.getItemAtPosition(newPos).isEmpty then

      GameResult.success(gameSession.updateWorld(world.updatePlayer(world.player.move(newPos))))
    else if currentRoom.getDoorPosition(param) == newPos &&
      currentRoom.exits.contains(param) then
      val neighbor = world
        .getNeighbor(param, currentRoom.id)
        .getOrElse(
          throw new IllegalStateException("Room" + currentRoom.id + "must have neighbor at " + param)
        )

      val entrance = neighbor
        .getDoorPosition(param.opposite)
        .moveBy(param.vector)

      GameResult.success(
        gameSession
          .updateGameState(gameState.setRoom(neighbor.id))
          .updateWorld(world
            .updateRoom(
              neighbor.withEnemies(
                neighbor.enemies.map(e =>
                  if e.position == entrance then
                    e.move(e.position.moveBy(param.vector))
                  else e
                )
              )
            )
            .updatePlayer(world.player.move(entrance))
          )
      )

    else GameResult.error(GameError.InvalidInput(param.toString), "You can't go this way")
