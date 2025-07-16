package scalata.application.usecases.playerusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.util.{Direction, GameError, GameResult}
import scalata.domain.world.GameSession

/** Handles a single **movement command** issued by the player.
 *
 * <h4>Decision tree</h4>
 * <ul>
 * <li><b>Simple step</b> – if the target tile lies inside the current room
 * and is free of living enemies/items, update the player’s position.</li>
 * <li><b>Door transition</b> – when the tile matches the current door and
 * the room contains an exit in that direction, move the player to the
 * neighbouring room, advance <code>GameState.currentRoom</code> and
 * push any enemy standing on the entrance one tile inward.</li>
 * <li><b>Invalid</b> – in all other cases return
 * <code>GameError.InvalidInput</code>.</li>
 * </ul>
 */
class PlayerMovementUseCase
    extends CreatureUseCase[GameResult[
      GameSession
    ], Direction]:

  /** Attempt to move the player one tile towards <code>direction</code>.
   *
   * @param direction   cardinal step requested by the user
   * @param gameSession immutable snapshot before the move
   * @return updated session wrapped in <code>GameResult.success</code> or a
   *         <code>GameResult.error</code> when the move is illegal
   */
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
