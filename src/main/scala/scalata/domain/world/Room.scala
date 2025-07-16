package scalata.domain.world

import scalata.domain.entities.{Enemy, Item}
import scalata.domain.util.Direction
import scalata.domain.util.Geometry.Point2D

/** Immutable description of a rectangular room on a floor.
 *
 * <h4>Structure</h4>
 * <ul>
 * <li><b>id</b> unique room identifier (used by world graph).</li>
 * <li><b>topLeft / botRight</b> corner coordinates that delimit the rectangle.</li>
 * <li><b>exits</b> map <code>Direction → roomId</code> for door connectivity.</li>
 * <li><b>items</b> loot on the ground.</li>
 * <li><b>enemies</b> creatures currently inhabiting the room.</li>
 * </ul>
 *
 * <h4>Main operations</h4>
 * <ul>
 * <li>Geometry helpers (<code>size</code>, border checks, <code>isInside</code>).</li>
 * <li>Topology helpers (<code>getNeighbor</code>, <code>getDoorPosition</code>).</li>
 * <li>Pure mutators that return a new room (<code>withItems</code>, <code>withEnemies</code>, <code>removeItem</code>).</li>
 * <li>Queries for entities at a given coordinate.</li>
 * </ul>
 */
final case class Room(
    id: String,
    topLeft: Point2D,
    botRight: Point2D,
    exits: Map[Direction, String], // direction -> roomId
    items: List[Item] = List.empty,
    enemies: List[Enemy] = List.empty
):

  def size: (Int, Int) =
    (this.botRight.x - this.topLeft.x, this.botRight.y - this.topLeft.y)

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

  def withItems(items: List[Item]): Room = copy(items = items)

  def withEnemies(enemies: List[Enemy]): Room = copy(enemies = enemies)

  def removeItem(item: Item): Room = copy(items = items.filterNot(_ == item))

  def getAliveEnemies: List[Enemy] = enemies.filter(_.isAlive)

  def getEnemyAtPosition(position: Point2D): Option[Enemy] =
    this.enemies.find(e => e.position == position)

  def getAliveEnemyAtPosition(position: Point2D): Option[Enemy] =
    this.enemies.find(e => e.position == position && e.isAlive)

  def getItemAtPosition(position: Point2D): Option[Item] =
    this.items.find(e => e.position.contains(position))
