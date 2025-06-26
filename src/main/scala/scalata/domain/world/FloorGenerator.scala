package scalata.domain.world

import scalata.domain.entities.Player
import scalata.domain.util.{MAX_PADDING, MIN_PADDING, NUM_ROWS_DUNGEON, Point2D, ROOMS, WORLD_DIMENSIONS}

import scala.util.Random

object FloorGenerator:
  def generateFloor(player: Player, difficulty: Int, seed: Long): World =
    Random.setSeed(seed)
    val numRoomsFloor = (ROOMS.length + NUM_ROWS_DUNGEON - 1) / NUM_ROWS_DUNGEON
    val shuffledRooms = Random.shuffle(ROOMS)

    val matrixRooms: List[List[String]] = shuffledRooms.grouped(numRoomsFloor).toList
    val rooms = generateRooms(difficulty, numRoomsFloor, shuffledRooms, matrixRooms)

    World(
      player,
      difficulty,
      Map.empty,
      matrixRooms
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
      val startRow = colIndex * areaWidth + Random.between(MIN_PADDING, MAX_PADDING)
      val endRow = (colIndex + 1) * areaWidth - Random.between(MIN_PADDING, MAX_PADDING)
      val startCol = rowIndex * areaHeight + Random.between(MIN_PADDING, MAX_PADDING)
      val endCol = (rowIndex + 1) * areaHeight - Random.between(MIN_PADDING, MAX_PADDING)

      //val connections = getConnections(matrixRooms, rowIndex, colIndex)

      roomName -> Room(
        roomName,
        Point2D(startRow, startCol),
        Point2D(endRow, endCol),
        // TODO List.empty,
        // TODO List.empty,
        Map.empty
      )
    ).toMap
