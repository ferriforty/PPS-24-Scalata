package scalata.domain.world

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.factories.PlayerFactory
import scalata.domain.util.GameError
import scalata.domain.util.GameError.UndoError
import scalata.domain.util.Geometry.Point2D
import scalata.domain.entities.Player
import scalata.domain.util.PlayerClasses.Mage

class GameSessionTest extends AnyFlatSpec with Matchers:

  val roomA: Room = Room("A", Point2D(0, 0), Point2D(3, 3), Map.empty)
  val world: World = World(
    player = PlayerFactory().create(Mage),
    rooms = Map("A" -> roomA),
    roomsArrangement = List(List("A"))
  )

  val gameState: GameState = GameState("", Set.empty, 1, "initialState")
  val updatedState: GameState = GameState("", Set.empty, 1, "Updated!")

  "GameSession.init" should "create a valid session with initial history" in:
    val session = GameSession.init(world, gameState)
    session.getWorld shouldBe world
    session.getGameState shouldBe gameState
    session.history.head shouldBe (world, gameState)

  "updateWorld" should "return a new GameSession with updated world" in:
    val newWorld = world.updatePlayer(world.player.copy(health = 50))
    val session = GameSession.init(world, gameState).updateWorld(newWorld)
    session.getWorld shouldBe newWorld
    session.getWorld.player.health shouldBe 50

  "updateGameState" should "return a new GameSession with updated state" in:
    val session =
      GameSession.init(world, gameState).updateGameState(updatedState)
    session.getGameState shouldBe updatedState

  "store" should "add the current session to the history" in:
    val session = GameSession.init(world, gameState)
    val updated = session
      .updateGameState(updatedState)
      .store

    updated.history.head shouldBe (world, updatedState)
    updated.history.tail.head shouldBe (world, gameState)

  "undo" should "revert to previous state if history has more than one entry" in:
    val session = GameSession
      .init(world, gameState)
      .updateGameState(updatedState)
      .store
      .undo

    session.getGameState shouldBe gameState

  "undo" should "return session with UndoError if no previous history exists" in:
    val session = GameSession.init(world, gameState)
    val undone = session.undo
    undone.getGameState.note should include(UndoError().message)
