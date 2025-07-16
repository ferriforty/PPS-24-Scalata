package scalata.application.services

import cats.effect.IO
import org.scalatest.{BeforeAndAfter, Tag}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.factories.{EnemyFactory, PlayerFactory}
import scalata.domain.entities.Player
import scalata.domain.util.PlayerClasses.Mage
import scalata.domain.util.*
import scalata.domain.util.Direction.South
import scalata.domain.util.EnemyClasses.Pig
import scalata.domain.util.Geometry.Point2D
import scalata.domain.world.GameSession
import scalata.application.usecases.GameRunningUseCase
import scalata.infrastructure.cli.view.TestView
import scalata.infrastructure.view.terminal.GameRunView

class FloorGeneratorTest extends AnyFlatSpec with Matchers with BeforeAndAfter:

  val TestPlayer: Player = PlayerFactory().create(Mage)
  val TestDifficulty = 1
  val TestLevel = 1
  var gameSession: GameSession = _

  before:
    gameSession =
      FloorGenerator.generateFloor(TestPlayer, TestDifficulty, 0, TestLevel)

  "FloorGenerator" should "maintain correct player state" in:
    gameSession.getWorld.player.name shouldBe TestPlayer.name
    gameSession.getWorld.player.role shouldBe TestPlayer.role

  "FloorGenerator" should "preserve difficulty level" in:
    gameSession.getWorld.difficulty shouldBe TestDifficulty

  "FloorGenerator" should "preserve level" in:
    gameSession.getGameState.currentLevel shouldBe TestLevel

  "FloorGenerator" should "generate correct room matrix structure" in:
    gameSession.getWorld.roomsArrangement should have size NUM_ROWS_DUNGEON
    gameSession.getWorld.roomsArrangement.foreach: row =>
      row should have size (ROOMS.length / NUM_ROWS_DUNGEON)

  "FloorGenerator" should "calculate valid room boundaries" in:
    val areaWidth = WORLD_DIMENSIONS._1 / (ROOMS.length / NUM_ROWS_DUNGEON)
    val areaHeight = WORLD_DIMENSIONS._2 / NUM_ROWS_DUNGEON

    gameSession.getWorld.roomsArrangement.zipWithIndex.foreach: (row, rowIdx) =>
      row.zipWithIndex.foreach: (roomName, colIdx) =>
        val room = gameSession.getWorld.rooms(roomName)

        room.topLeft.x should (be >= (colIdx * areaWidth + MIN_PADDING)
          and be <= (colIdx * areaWidth + MAX_PADDING))
        room.botRight.x should (be >= ((colIdx + 1) * areaWidth - MAX_PADDING)
          and be <= ((colIdx + 1) * areaWidth - MIN_PADDING))

  "FloorGenerator" should "create valid room connections" in:
    gameSession.getWorld.roomsArrangement.zipWithIndex.foreach: (row, rowIdx) =>
      row.zipWithIndex.foreach: (roomName, colIdx) =>
        val connections = gameSession.getWorld.rooms(roomName).exits

        if colIdx > 0 then
          connections.get(Direction.West) shouldBe Some(row(colIdx - 1))
        else connections.get(Direction.West) shouldBe None

  "FloorGenerator" should "produce deterministic results with same seed" in:
    val world1 = FloorGenerator
      .generateFloor(TestPlayer, TestDifficulty, 42, TestLevel)
      .getWorld
    val world2 = FloorGenerator
      .generateFloor(TestPlayer, TestDifficulty, 42, TestLevel)
      .getWorld
    world1 shouldBe world2

  "FloorGenerator" should "generate exit Door in the last room" in:
    assert(
      gameSession.getWorld.rooms
        .filter(r => r._1 == gameSession.getWorld.roomsArrangement.last.last)
        .exists(p =>
          p._2.items.exists(i => i.itemClass == ItemClasses.ExitDoor)
        )
    )

  "FloorGenerator" should "generate Sign in the last room" in:
    assert(
      gameSession.getWorld.rooms
        .filter(r => r._1 == gameSession.getWorld.roomsArrangement.head.head)
        .exists(p => p._2.items.exists(i => i.itemClass == ItemClasses.Sign))
    )

  it should "generate in less then 100ms" taggedAs Tag("Non-Functional") in:
    val start = System.nanoTime()
    FloorGenerator.generateFloor(TestPlayer, TestDifficulty, 0, TestLevel)
    val ms = (System.nanoTime() - start) / 1e6
    assert(ms < 100)

  it should "handle 1000 enemies" taggedAs Tag("Non-Functional") in:
    val currentRoom = gameSession.getWorld
      .getRoom(
        gameSession.getGameState.currentRoom
      )
      .get

    val enemies = for
      i <- 1 to 1000
      enemy = EnemyFactory()
        .create(Pig)
        .move(
          currentRoom.botRight.moveBy(
            Point2D(-(i % currentRoom.size._1), -(i % currentRoom.size._2))
          )
        )
    yield enemy

    val filledGS = gameSession.updateWorld(
      gameSession.getWorld.updateRoom(
        currentRoom.withEnemies(enemies.toList)
      )
    )

    val start = System.nanoTime()
    GameRunningUseCase().execTurn(
      filledGS,
      IO.pure(PlayerCommand.Movement(South))
    )

    val usedMemory =
      (Runtime.getRuntime.totalMemory - Runtime.getRuntime.freeMemory) / (1024 * 1024)
    val timeMs = (System.nanoTime() - start) / 1e6
    assert(usedMemory < 512)
    assert(timeMs < 2000)
