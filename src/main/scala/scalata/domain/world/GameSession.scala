package scalata.domain.world

import cats.syntax.all.*
import cats.data.NonEmptyList
import scalata.domain.util.GameError

final case class GameSession(
    world: World,
    gameState: GameState,
    history: NonEmptyList[(World, GameState)]
):

  def getWorld: World = this.world
  def getGameState: GameState = this.gameState
  private def getSession: (World, GameState) =
    (this.getWorld, this.getGameState)

  def updateWorld(world: World): GameSession = copy(world = world)
  def updateGameState(gameState: GameState): GameSession =
    copy(gameState = gameState)

  def store: GameSession =
    copy(history = NonEmptyList(getSession, history.toList))

  def undo: GameSession =
    history.tail.toNel.fold(
      this.updateGameState(
        this.getGameState.withNote(GameError.UndoError().message)
      )
    ): t =>
      copy(
        world = t.head._1,
        gameState = t.head._2,
        history = t
      )

object GameSession:
  def init(world: World, state: GameState): GameSession =
    GameSession(world, state, NonEmptyList.one((world, state)))
