package scalata.domain.util

import scala.util.Random

// Recommended constants, a smaller world would be unplayable
val WORLD_DIMENSIONS = (50, 20)
val NUM_ROWS_DUNGEON = 2
val MAX_PADDING = WORLD_DIMENSIONS._2 / (NUM_ROWS_DUNGEON * 2 + 1)
val MIN_PADDING = 1
val MAX_DIFFICULTY = 10.0
val MIN_ENEMIES = 1.0
val MAX_ENEMIES = 3.0
val POTION_WEIGHT = 0.65
val DUST_WEIGHT = 0.4

def gaussianBetween(from: Double, to: Double, difficulty: Int) =
  val mean = from + (difficulty / MAX_DIFFICULTY) * (to - from)
  val stdDev = 0.8

  val gaussian = Random.nextGaussian()
  val raw = mean + gaussian * stdDev

  raw.round.toInt.max(from.toInt).min(to.toInt)

def weightedRandom(p: Double): Boolean =
  Random.nextDouble() < p

val ROOMS: List[String] = List(
  "Hall of Chains",
  "Silent Crypt",
  "Alchemist’s Laboratory",
  "Abandoned Forge",
  "Well of Souls",
  "Broken Conservatory",
  "Hall of Shattered Mirrors",
  "Cursed Library"
)
