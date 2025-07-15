package scalata.domain.world

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.factories.PlayerFactory
import scalata.domain.entities.Player
import scalata.domain.util.Direction
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.PlayerClasses.Mage

class WorldTest extends AnyFlatSpec with Matchers:

  val player: Player = PlayerFactory().create(Mage)

  val roomA: Room = Room(
    id = "A",
    topLeft = Point2D(0, 0),
    botRight = Point2D(0, 0),
    exits = Map(Direction.East -> "B"),
    items = List.empty,
    enemies = List.empty
  )
  val roomB: Room = Room(
    id = "B",
    topLeft = Point2D(0, 0),
    botRight = Point2D(0, 0),
    exits = Map(Direction.West -> "A"),
    items = List.empty,
    enemies = List.empty
  )
  val roomMap: Map[String, Room] = Map("A" -> roomA, "B" -> roomB)
  val arrangement: List[List[String]] = List(List("A", "B"))

  val world: World = World(
    player = player,
    difficulty = 2,
    rooms = roomMap,
    roomsArrangement = arrangement
  )

  "World" should "return its difficulty" in:
    world.getDifficulty shouldBe 2

  it should "return its player" in:
    world.getPlayer shouldBe player

  it should "retrieve rooms by id" in:
    world.getRoom("A") shouldBe Some(roomA)
    world.getRoom("X") shouldBe None

  it should "update a room" in:
    val newRoomA = roomA.copy(exits = roomA.exits + (Direction.North -> "B"))
    val updatedWorld = world.updateRoom(newRoomA)
    updatedWorld.getRoom("A") shouldBe Some(newRoomA)

  it should "update the player" in:
    val newPlayer = player.copy(health = 50)
    val updatedWorld = world.updatePlayer(newPlayer)
    updatedWorld.getPlayer shouldBe newPlayer

  it should "return the neighbor room in a given direction" in:
    world.getNeighbor(Direction.East, "A").map(_.id) shouldBe Some("B")
    world.getNeighbor(Direction.West, "A") shouldBe None
    world.getNeighbor(Direction.West, "B").map(_.id) shouldBe Some("A")
    world.getNeighbor(Direction.North, "A") shouldBe None
