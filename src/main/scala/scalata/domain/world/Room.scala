package scalata.domain.world

import scalata.domain.util.{Direction, Point2D}

final case class Room(
    id: String,
    topLeft: Point2D,
    botRight: Point2D,
    // TODO items: List[Items],
    // TODO enemies: List[Enemy],
    exits: Map[Direction, String] // direction -> roomId
):
  def getNeighbor(direction: Direction): Option[String] =
    this.exits.get(direction)

  def isSideBorder(point: Point2D): Boolean =
    point.y >= topLeft.y && point.y <= botRight.y &&
      (topLeft.x == point.x || botRight.x == point.x)

  def isTopBotBorder(point: Point2D): Boolean =
    point.x >= topLeft.x && point.x <= botRight.x &&
      (topLeft.y == point.y || botRight.y == point.y)

  def isInside(point: Point2D): Boolean =
    point.x > topLeft.x && point.x < botRight.x &&
      point.y > topLeft.y && point.y < botRight.y

  def getDoorPosition(where: Direction): Point2D =
    val centerX = ((botRight.x - topLeft.x) / 2) + topLeft.x
    val centerY = ((botRight.y - topLeft.y) / 2) + topLeft.y
    where match
      case Direction.North => Point2D(centerX, topLeft.y)
      case Direction.South => Point2D(centerX, botRight.y)
      case Direction.West  => Point2D(topLeft.x, centerY)
      case Direction.East  => Point2D(botRight.x, centerY)
