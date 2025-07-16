package scalata.domain.entities.components

import scalata.domain.entities.Item
import scalata.domain.world.GameSession

/** Mixin for every item that the player can **pick up**.
  *
  * <ul> <li><b>pick</b> removes the item from the current room and adds it to
  * the player’s inventory, returning an updated
  * [[scalata.domain.world.GameSession GameSession]].</li> <li>If the room
  * referenced by <code>GameState.currentRoom</code> cannot be found (world
  * corruption), the method throws <code>IllegalStateException</code>.</li>
  * </ul>
  */
trait Pickable:

  /** Transfer <code>item</code> from floor to inventory.
    *
    * @param item
    *   the item being collected
    * @param gameSession
    *   current session snapshot
    * @return
    *   a new session with world and player updated
    */
  def pick(item: Item, gameSession: GameSession): GameSession =
    (for
      room <- gameSession.getWorld.getRoom(gameSession.getGameState.currentRoom)
      player = gameSession.getWorld.getPlayer
    yield gameSession.updateWorld(
      gameSession.getWorld
        .updateRoom(room.removeItem(item))
        .updatePlayer(player.addItem(item))
    )).getOrElse(
      throw new IllegalStateException(
        "current Room in game state doesn't exists"
      )
    )
