package scalata.domain.world

import scalata.domain.entities.Player
import scalata.domain.util.{NUM_ROWS_DUNGEON, ROOMS, WORLD_DIMENSIONS}

import scala.util.Random

object FloorGenerator:
  def generateFloor(player: Player, difficulty: Int, seed: Long): World =
    Random.setSeed(seed)

    val rooms = generateRooms(1)

    World(
      player,
      difficulty,
      Map.empty,
      List.empty
    )

  private def generateRooms(difficulty: Int): Map[String, Room] =

    val numRoomsFloor = (ROOMS.length + NUM_ROWS_DUNGEON - 1) / NUM_ROWS_DUNGEON

    val areaHeight = WORLD_DIMENSIONS._2 / NUM_ROWS_DUNGEON
    val areaWidth = WORLD_DIMENSIONS._1 / numRoomsFloor

    val matrixRooms: List[List[String]] = roomsArrangement(numRoomsFloor)

    Map.empty

  private def roomsArrangement(numRoomsFloor: Int): List[List[String]] =
    val shuffledRooms = Random.shuffle(ROOMS)

    (for (i <- 0 until NUM_ROWS_DUNGEON) yield
      val startIndex = i * numRoomsFloor
      val numCols = math.min(numRoomsFloor, shuffledRooms.length - startIndex)
      (for (j <- 0 until numCols) yield shuffledRooms(startIndex + j)).toList
    ).toList


