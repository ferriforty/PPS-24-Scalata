package scalata.application.usecases.playerusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.util.{Direction, GameError, GameResult}
import scalata.domain.world.GameSession

class PlayerMovementUseCase
    extends CreatureUseCase[GameResult[
      GameSession
    ], Direction]:
  override def execute(
      direction: Direction,
      gameSession: GameSession
  ): GameResult[GameSession] =
    val world = gameSession.getWorld
    val gameState = gameSession.getGameState
    val currentRoom = world
      .getRoom(gameState.currentRoom)
      .getOrElse(
        throw new IllegalStateException(
          "Room Not defined in movement use case"
        )
      )
    val newPos = world.player.position.moveBy(direction.vector)

    if currentRoom.isInside(newPos) &&
      currentRoom.getAliveEnemyAtPosition(newPos).isEmpty &&
      currentRoom.getItemAtPosition(newPos).isEmpty
    then

      GameResult.success(
        gameSession.updateWorld(world.updatePlayer(world.player.move(newPos)))
      )
    else if currentRoom.getDoorPosition(direction) == newPos &&
      currentRoom.exits.contains(direction)
    then
      val neighbor = world
        .getNeighbor(direction, currentRoom.id)
        .getOrElse(
          throw new IllegalStateException(
            "Room" + currentRoom.id + "must have neighbor at " + direction
          )
        )

      val entrance = neighbor
        .getDoorPosition(direction.opposite)
        .moveBy(direction.vector)

      GameResult.success(
        gameSession
          .updateGameState(gameState.setRoom(neighbor.id))
          .updateWorld(
            world
              .updateRoom(
                neighbor.withEnemies(
                  neighbor.enemies.map(e =>
                    if e.position == entrance then
                      e.move(e.position.moveBy(direction.vector))
                    else e
                  )
                )
              )
              .updatePlayer(world.player.move(entrance))
          )
      )
    else GameResult.error(GameError.InvalidInput(direction.toString))
