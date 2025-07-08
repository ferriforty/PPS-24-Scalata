package scalata.domain.entities.components

import scalata.domain.entities.Item
import scalata.domain.world.GameSession

trait Pickable:

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
