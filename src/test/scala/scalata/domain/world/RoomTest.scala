package scalata.domain.world

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.factories.{EnemyFactory, ItemFactory}
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.Direction
import scalata.domain.entities.{Enemy, Item}
import scalata.domain.util.EnemyClasses.{Goblin, Pig}
import scalata.domain.util.ItemClasses.Sign

class RoomTest extends AnyFlatSpec with Matchers:

  val topLeft: Point2D = Point2D(0, 0)
  val botRight: Point2D = Point2D(4, 4)
  val exits: Map[Direction, String] =
    Map(Direction.North -> "N", Direction.East -> "E")

  val enemy1Pos: Point2D = Point2D(2, 3)
  val enemyDeadPos: Point2D = Point2D(3, 1)

  val itemPos: Point2D = Point2D(1, 1)

  val item1: Item = ItemFactory().create(Sign).spawn(Some(itemPos))
  val enemy1: Enemy = EnemyFactory().create(Pig).move(enemy1Pos)
  val enemyDead: Enemy = EnemyFactory().create(Goblin).move(enemyDeadPos)

  val room: Room = Room(
    id = "room1",
    topLeft = topLeft,
    botRight = botRight,
    exits = exits,
    items = List(item1),
    enemies = List(enemy1, enemyDead.takeDamage(enemyDead.maxHealth))
  )

  "size" should "return correct width and height" in:
    room.size shouldEqual (4, 4)

  "getNeighbor" should "return the correct neighbor room id" in:
    room.getNeighbor(Direction.North) shouldBe Some("N")
    room.getNeighbor(Direction.South) shouldBe None

  "isSideBorder" should "detect sides correctly" in:
    room.isSideBorder(Point2D(0, 2)) shouldBe true
    room.isSideBorder(Point2D(4, 3)) shouldBe true
    room.isSideBorder(Point2D(2, 2)) shouldBe false

  "isTopBotBorder" should "detect top/bottom correctly" in:
    room.isTopBotBorder(Point2D(3, 0)) shouldBe true
    room.isTopBotBorder(Point2D(1, 4)) shouldBe true
    room.isTopBotBorder(Point2D(2, 2)) shouldBe false

  "isInside" should "verify point is fully inside the room (not on border)" in:
    room.isInside(Point2D(2, 2)) shouldBe true
    room.isInside(Point2D(0, 0)) shouldBe false
    room.isInside(Point2D(4, 4)) shouldBe false

  "getDoorPosition" should "calculate correct door positions" in:
    room.getDoorPosition(Direction.North) shouldBe Point2D(2, 0)
    room.getDoorPosition(Direction.South) shouldBe Point2D(2, 4)
    room.getDoorPosition(Direction.West) shouldBe Point2D(0, 2)
    room.getDoorPosition(Direction.East) shouldBe Point2D(4, 2)

  "withItems" should "update the list of items" in:
    val updated = room.withItems(List(item1))
    updated.items shouldEqual List(item1)

  "removeItem" should "remove the specified item" in:
    val removed = room.removeItem(item1)
    removed.items.contains(item1) shouldBe false

  "enemies" should "contain all Enemies" in:
    room.enemies shouldEqual List(
      enemy1,
      enemyDead.takeDamage(enemyDead.maxHealth)
    )

  "getAliveEnemies" should "return only enemies that are alive" in:
    room.getAliveEnemies shouldEqual List(enemy1)

  "getEnemyAtPosition" should "find any enemy at given position" in:
    room.getEnemyAtPosition(enemy1Pos) shouldBe Some(enemy1)
    room.getEnemyAtPosition(Point2D(5, 5)) shouldBe None

  "getAliveEnemyAtPosition" should "return alive enemy at position" in:
    room.getAliveEnemyAtPosition(Point2D(2, 3)) shouldBe Some(enemy1)
    room.getAliveEnemyAtPosition(Point2D(3, 1)) shouldBe None

  "getItemAtPosition" should "return an item at a position if exists" in:
    room.getItemAtPosition(itemPos) shouldBe Some(item1)
    room.getItemAtPosition(Point2D(3, 3)) shouldBe None
