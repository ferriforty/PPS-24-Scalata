package scalata.domain.world

import scalata.domain.entities.Player
import scalata.domain.util.{
  Direction,
  MAX_PADDING,
  MIN_PADDING,
  NUM_ROWS_DUNGEON,
  Point2D,
  ROOMS,
  WORLD_DIMENSIONS
}

import scala.util.Random

object FloorGenerator:
  def generateFloor(player: Player, difficulty: Int, seed: Long, level: Int): GameSession =
    Random.setSeed(seed)
    val numRoomsFloor = (ROOMS.length + NUM_ROWS_DUNGEON - 1) / NUM_ROWS_DUNGEON
    val shuffledRooms = Random.shuffle(ROOMS)

    val matrixRooms: List[List[String]] =
      shuffledRooms.grouped(numRoomsFloor).toList
    val rooms =
      generateRooms(difficulty, numRoomsFloor, shuffledRooms, matrixRooms)

    val startRoom = matrixRooms.head.head

    GameSession(World(
        player,
        difficulty,
        rooms,
        matrixRooms
      ),
      GameState(
        currentRoom = startRoom,
        visitedRooms = Set(startRoom),
        currentLevel = level)
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
      roomName -> Room(
        roomName,
        Point2D(startRow, startCol),
        Point2D(endRow, endCol),
        // TODO List.empty,
        // TODO List.empty,
        connections
      )
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

  private def calculateStartEnd(index: Int, size: Int): (Int, Int) = {
    val start = index * size + Random.between(MIN_PADDING, MAX_PADDING)
    val end = (index + 1) * size - Random.between(MIN_PADDING, MAX_PADDING)
    (start, end)
  }
