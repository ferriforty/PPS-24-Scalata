package scalata.application.services

import scalata.application.services.factories.{EnemyFactory, ItemFactory}
import scalata.domain.entities.{Enemy, Item, Player}
import scalata.domain.util.*
import scalata.domain.util.Geometry.Point2D
import scalata.domain.world.{GameSession, GameState, Room, World}

import scala.util.Random

/** Procedural generator that builds one <b>tower floor</b> together with the
  * initial [[GameSession]] snapshot for that level.
  *
  * <h4>Overview</h4> <ol> <li>Shuffle room names and arrange them in a
  * <code>matrixRooms</code> grid (rows&nbsp;×&nbsp;columns) so the dungeon is
  * always rectangular.</li> <li>Create every [[Room]] with random padding so
  * shapes vary between runs.</li> <li>Populate rooms (except the spawn room)
  * with enemies and items whose number scales with
  * <code>difficulty</code>.</li> <li>Insert a <i>Sign</i> in the spawn room and
  * an <i>Exit Door</i> in the last room to let the player advance to the next
  * floor.</li> <li>Return a fully initialised [[GameSession]] that already
  * contains one entry in the undo history (<code>GameSession.init</code>).</li>
  * </ol>
  */
object FloorGenerator:

  /** Create a new floor and wrap it in a [[GameSession]].
    *
    * @param player
    *   hero to place in the starting room
    * @param difficulty
    *   global difficulty (1–10) controlling spawn rates
    * @param seed
    *   random seed for reproducible layouts
    * @param level
    *   tower floor index (starts at&nbsp;1)
    */
  def generateFloor(
      player: Player,
      difficulty: Int,
      seed: Long,
      level: Int
  ): GameSession =
    Random.setSeed(seed)
    val numRoomsFloor = (ROOMS.length + NUM_ROWS_DUNGEON - 1) / NUM_ROWS_DUNGEON
    val shuffledRooms = Random.shuffle(ROOMS)

    val matrixRooms: List[List[String]] =
      shuffledRooms.grouped(numRoomsFloor).toList
    val rooms =
      generateRooms(difficulty, numRoomsFloor, shuffledRooms, matrixRooms)

    val startRoom = matrixRooms.head.head

    GameSession.init(
      World(
        player.move(rooms(startRoom).topLeft.moveBy(Point2D(1, 1))),
        difficulty,
        rooms,
        matrixRooms
      ),
      GameState(
        currentRoom = startRoom,
        visitedRooms = Set(startRoom),
        currentLevel = level
      )
    )

  private def generateRooms(
      difficulty: Int,
      numRoomsFloor: Int,
      shuffledRooms: List[String],
      matrixRooms: List[List[String]]
  ): Map[String, Room] =

    val areaHeight = WORLD_DIMENSIONS._2 / NUM_ROWS_DUNGEON
    val areaWidth = WORLD_DIMENSIONS._1 / numRoomsFloor
    (for
      (row, rowIndex) <- matrixRooms.zipWithIndex
      (roomName, colIndex) <- row.zipWithIndex
    yield
      val (startRow, endRow) = calculateStartEnd(colIndex, areaWidth)
      val (startCol, endCol) = calculateStartEnd(rowIndex, areaHeight)

      val connections = getConnections(matrixRooms, rowIndex, colIndex)

      val room = Room(
        roomName,
        Point2D(startRow, startCol),
        Point2D(endRow, endCol),
        connections,
        List.empty,
        List.empty
      )

      val enemies =
        if room.id == matrixRooms.head.head then List.empty
        else generateEnemies(room, matrixRooms, difficulty)

      val items =
        if room.id == matrixRooms.head.head then
          List(
            ItemFactory()
              .create(ItemClasses.Sign, room.id + "-i1")
              .spawn(
                Some(
                  room
                    .getDoorPosition(Direction.North)
                    .moveBy(Direction.North.doorMat)
                )
              )
          )
        else generateItems(room, difficulty, enemies)

      roomName -> room
        .withEnemies(enemies)
        .withItems:
          if room.id == matrixRooms.last.last then
            items.appended(
              ItemFactory()
                .create(
                  ItemClasses.ExitDoor,
                  room.id + "-i" + (items.size + 1)
                )
                .spawn(Some(room.botRight.moveBy(Point2D(-1, -1))))
            )
          else items
    ).toMap

  private def getConnections(
      matrixRooms: List[List[String]],
      row: Int,
      col: Int
  ): Map[Direction, String] =
    Direction.values
      .flatMap(
        _ match
          case Direction.West if col > 0 =>
            matrixRooms(row).lift(col - 1).map(Direction.West -> _)
          case Direction.East =>
            matrixRooms(row).lift(col + 1).map(Direction.East -> _)
          case Direction.North if row > 0 =>
            matrixRooms
              .lift(row - 1)
              .flatMap(_.lift(col))
              .map(Direction.North -> _)
          case Direction.South =>
            matrixRooms
              .lift(row + 1)
              .flatMap(_.lift(col))
              .map(Direction.South -> _)
          case _ => None
      )
      .toMap

  private def generateItems(
      room: Room,
      difficulty: Int,
      enemies: List[Enemy]
  ): List[Item] =

    val itemPosition = Random
      .shuffle(for
        x <- room.topLeft.x + 1 until room.botRight.x
        y <- room.topLeft.y + 1 until room.botRight.y
        if !enemies.exists(e => e.position == Point2D(x, y)) &&
          !room.exits.exists(d =>
            room.getDoorPosition(d._1).moveBy(d._1.doorMat) == Point2D(x, y)
          )
      yield Point2D(x, y))
      .head

    List(
      ItemFactory()
        .createBox(
          difficulty,
          room.id + "-i" + (room.items.size + 1)
        )
        .spawn(Some(itemPosition))
    )

  private def generateEnemies(
      room: Room,
      matrixRooms: List[List[String]],
      difficulty: Int
  ): List[Enemy] =

    val numEnemies = gaussianBetween(MIN_ENEMIES, MAX_ENEMIES, difficulty)
    val enemiesPosition = Random
      .shuffle(for
        x <- room.topLeft.x + 1 until room.botRight.x
        y <- room.topLeft.y + 1 until room.botRight.y
        if !room.items.exists(_.position == Point2D(x, y))
        if !(room.id == matrixRooms.last.last && Point2D(x, y) ==
          room.botRight.moveBy(Point2D(-1, -1)))
      yield Point2D(x, y))
      .take(numEnemies)

    enemiesPosition.zipWithIndex
      .map((p, i) =>
        EnemyFactory()
          .randomGeneration(
            "room" + matrixRooms.zipWithIndex
              .map((l, j) => l.indexOf(room.id).toString + j + "e")
              .filterNot(r => r.contains("-1"))
              .head + i
          )
          .move(p)
      )
      .toList

  private def calculateStartEnd(index: Int, size: Int): (Int, Int) =
    val start = index * size + Random.between(MIN_PADDING, MAX_PADDING)
    val end = (index + 1) * size - Random.between(MIN_PADDING, MAX_PADDING)
    (start, end)
