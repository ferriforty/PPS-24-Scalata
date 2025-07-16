package scalata.domain.util

import scala.util.Random

// Recommended constants, a smaller world would be unplayable
/* ── World layout ────────────────────────────────────────── */
val WORLD_DIMENSIONS = (50, 20)
val NUM_ROWS_DUNGEON = 2
val MAX_PADDING = WORLD_DIMENSIONS._2 / (NUM_ROWS_DUNGEON * 2 + 1)
val MIN_PADDING = 1

/* ── Difficulty & spawning ───────────────────────────────── */
val MAX_DIFFICULTY = 10.0
val MIN_ENEMIES = 1.0
val MAX_ENEMIES = 3.0
val POTION_WEIGHT = 0.65
val DUST_WEIGHT = 0.4

/** Sample an integer in <code>[from, to]</code> following a Gaussian curve
  * whose mean shifts with <code>difficulty</code>.
  */
def gaussianBetween(from: Double, to: Double, difficulty: Int) =
  val mean = from + (difficulty / MAX_DIFFICULTY) * (to - from)
  val stdDev = 0.8

  val gaussian = Random.nextGaussian()
  val raw = mean + gaussian * stdDev

  raw.round.toInt.max(from.toInt).min(to.toInt)

/** Bernoulli trial with success probability <code>p</code>. */
def weightedRandom(p: Double): Boolean =
  Random.nextDouble() < p

/** Pool of room names used by the procedural generator. */
val ROOMS: List[String] = List(
  "Hall of Chains",
  "Silent Crypt",
  "Alchemists Laboratory",
  "Abandoned Forge",
  "Well of Souls",
  "Broken Conservatory",
  "Hall of Shattered Mirrors",
  "Cursed Library"
)
