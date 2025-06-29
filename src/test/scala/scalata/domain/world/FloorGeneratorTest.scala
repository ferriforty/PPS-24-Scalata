package scalata.domain.world

import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.domain.entities.Player
import scalata.domain.util.PlayerClasses.Mage
import scalata.domain.util.{
  Direction,
  MAX_PADDING,
  MIN_PADDING,
  NUM_ROWS_DUNGEON,
  Point2D,
  ROOMS,
  WORLD_DIMENSIONS
}

class FloorGeneratorTest extends AnyFlatSpec with Matchers with BeforeAndAfter:

  val TestPlayer: Player = Player(role = Mage, position = Point2D(0, 0))
  val TestDifficulty = 1
  var world: World = _

  before:
    world = FloorGenerator.generateFloor(TestPlayer, TestDifficulty, 0)

  "FloorGenerator" should "maintain correct player state" in:
    world.player shouldBe TestPlayer

  "FloorGenerator" should "preserve difficulty level" in:
    world.difficulty shouldBe TestDifficulty

  "FloorGenerator" should "generate correct room matrix structure" in:
    world.roomsArrangement should have size NUM_ROWS_DUNGEON
    world.roomsArrangement.foreach: row =>
      row should have size (ROOMS.length / NUM_ROWS_DUNGEON)

  "FloorGenerator" should "calculate valid room boundaries" in:
    val areaWidth = WORLD_DIMENSIONS._1 / (ROOMS.length / NUM_ROWS_DUNGEON)
    val areaHeight = WORLD_DIMENSIONS._2 / NUM_ROWS_DUNGEON

    world.roomsArrangement.zipWithIndex.foreach: (row, rowIdx) =>
      row.zipWithIndex.foreach: (roomName, colIdx) =>
        val room = world.rooms(roomName)

        room.topLeft.x should (be >= (colIdx * areaWidth + MIN_PADDING)
          and be <= (colIdx * areaWidth + MAX_PADDING))
        room.botRight.x should (be >= ((colIdx + 1) * areaWidth - MAX_PADDING)
          and be <= ((colIdx + 1) * areaWidth - MIN_PADDING))

  "FloorGenerator" should "create valid room connections" in:
    world.roomsArrangement.zipWithIndex.foreach: (row, rowIdx) =>
      row.zipWithIndex.foreach: (roomName, colIdx) =>
        val connections = world.rooms(roomName).exits

        if colIdx > 0 then
          connections.get(Direction.West) shouldBe Some(row(colIdx - 1))
        else connections.get(Direction.West) shouldBe None

  "FloorGenerator" should "produce deterministic results with same seed" in:
    val world1 = FloorGenerator.generateFloor(TestPlayer, TestDifficulty, 42)
    val world2 = FloorGenerator.generateFloor(TestPlayer, TestDifficulty, 42)
    world1 shouldBe world2
