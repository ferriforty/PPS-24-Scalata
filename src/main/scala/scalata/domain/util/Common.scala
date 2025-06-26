package scalata.domain.util

// Recommended constants, a smaller world would be unplayable
val WORLD_DIMENSIONS = (50, 20)
val NUM_ROWS_DUNGEON = 2
val MAX_PADDING = WORLD_DIMENSIONS._2 / (NUM_ROWS_DUNGEON * 2 + 1)
val MIN_PADDING = 1
val MAX_DIFFICULTY = 10

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