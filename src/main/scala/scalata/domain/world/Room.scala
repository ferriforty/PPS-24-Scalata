package scalata.domain.world

import scalata.domain.util.{Direction, Point2D}

final case class Room(
    id: String,
    topLeft: Point2D,
    botRight: Point2D,
    exits: Map[Direction, String] // direction -> roomId
):
  def getNeighbor(direction: Direction): Option[String] =
    this.exits.get(direction)
