package scalata.domain.world

import scalata.domain.util.{Direction, Point2D}

final case class Room(
                       name: String,
                       topLeft: Point2D,
                       botRight: Point2D,
                       exits: Map[Direction, Int] // direction -> roomId
                     )
