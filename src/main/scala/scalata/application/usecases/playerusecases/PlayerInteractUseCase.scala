package scalata.application.usecases.playerusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.util.{Direction, GameError, GameResult}
import scalata.domain.world.GameSession

/** Handles the **“interact”** command (pick up / open / read whatever lies on
  * the tile immediately in front of the player).
  *
  * <h4>Flow</h4> <ul> <li>Compute the target coordinate by adding
  * <code>direction.vector</code> to the player’s current position.</li> <li>If
  * an item is present, delegate to its <code>interact(GameSession)</code>
  * method and propagate the result.</li> <li>If the tile is empty, return
  * <code>GameResult.error(GameError.ItemNotPresent)</code>.</li> </ul>
  *
  * The method is pure: all updates are returned in a new
  * <code>GameSession</code>; no in-place mutation occurs.
  */
class PlayerInteractUseCase
    extends CreatureUseCase[GameResult[
      GameSession
    ], Direction]:

  /** Execute an interaction in the specified direction.
    *
    * @param direction
    *   cardinal direction chosen by the player
    * @param gameSession
    *   current immutable snapshot
    * @return
    *   a successful <code>GameResult</code> with the updated session, or an
    *   error signalling that no item is there
    */
  override def execute(
      direction: Direction,
      gameSession: GameSession
  ): GameResult[GameSession] =

    val currentRoom = gameSession.getWorld
      .getRoom(gameSession.getGameState.currentRoom)
      .getOrElse(
        throw IllegalStateException("Room Not defined in Interact use case")
      )

    val itemPos = gameSession.getWorld.player.position.moveBy(direction.vector)
    currentRoom
      .getItemAtPosition(itemPos)
      .fold(
        GameResult.error(GameError.ItemNotPresent())
      )(item => item.interact(gameSession))
