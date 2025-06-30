package scalata.domain.world

import scalata.domain.entities.Player

final case class GameSession(
    world: World,
    gameState: GameState
):

  def getWorld: World = this.world
  def getGameState: GameState = this.gameState

  def updateWorld(world: World): GameSession = copy(world = world)
  def updateGameState(gameState: GameState): GameSession = copy(gameState = gameState)

