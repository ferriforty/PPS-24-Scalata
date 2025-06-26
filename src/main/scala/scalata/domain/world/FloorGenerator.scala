package scalata.domain.world

import scalata.domain.entities.Player
import scalata.domain.util.{NUM_ROWS_DUNGEON, ROOMS, WORLD_DIMENSIONS}

import scala.util.Random

object FloorGenerator:
  def generateFloor(player: Player, difficulty: Int, seed: Long): World =
    Random.setSeed(seed)
    val numRoomsFloor = (ROOMS.length + NUM_ROWS_DUNGEON - 1) / NUM_ROWS_DUNGEON
    val shuffledRooms = Random.shuffle(ROOMS)

    val matrixRooms: List[List[String]] = roomsArrangement(numRoomsFloor, shuffledRooms)
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

    Map.empty

  private def roomsArrangement(numRoomsFloor: Int, shuffledRooms: List[String]): List[List[String]] =
    val shuffledRooms = Random.shuffle(ROOMS)

    (for (i <- 0 until NUM_ROWS_DUNGEON) yield
      val startIndex = i * numRoomsFloor
      val numCols = math.min(numRoomsFloor, shuffledRooms.length - startIndex)
      (for (j <- 0 until numCols) yield shuffledRooms(startIndex + j)).toList
    ).toList


