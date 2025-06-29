package scalata.domain.world

import scalata.domain.entities.Player

final case class GameSession(
    world: World,
    gameState: GameState
):

  def getWorld: World = this.world
  def getGameState: GameState = this.gameState
