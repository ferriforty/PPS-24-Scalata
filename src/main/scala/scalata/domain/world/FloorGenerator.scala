package scalata.domain.world

import scalata.domain.entities.Player

import scala.util.Random

object FloorGenerator:
  def generateFloor(player: Player, difficulty: Int, seed: Long): World =
    Random.setSeed(seed)

    World(
      player,
      difficulty,
      Map.empty
    )
