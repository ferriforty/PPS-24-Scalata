package scalata.domain.world

import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.factories.PlayerFactory
import scalata.domain.entities.Player
import scalata.domain.util.PlayerClasses.Mage
import scalata.domain.util.{
  Direction,
  ItemClasses,
  MAX_PADDING,
  MIN_PADDING,
  NUM_ROWS_DUNGEON,
  Geometry,
  ROOMS,
  WORLD_DIMENSIONS
}

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
