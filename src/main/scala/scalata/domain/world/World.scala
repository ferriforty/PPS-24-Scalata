package scalata.domain.world

final case class World(
                      difficulty: Int = 1,
                      rooms: Map[Int, Room]
                      )
