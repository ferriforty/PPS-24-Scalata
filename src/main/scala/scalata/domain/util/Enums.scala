package scalata.domain.util

import scalata.domain.util.Geometry.Point2D

/** All hero archetypes with combat stats. */
enum PlayerClasses:
  case Mage
  case Barbarian
  case Assassin

  def reach: Int =
    this match
      case PlayerClasses.Mage      => 2
      case PlayerClasses.Barbarian => 1
      case PlayerClasses.Assassin  => 1

  def visibility: Int =
    this match
      case PlayerClasses.Mage      => 6
      case PlayerClasses.Barbarian => 7
      case PlayerClasses.Assassin  => 4

/** Enemy families rendered by a single‐char glyph. */
enum EnemyClasses:
  case Goblin
  case Pig

  override def toString: String =
    this match
      case EnemyClasses.Goblin => "g"
      case EnemyClasses.Pig    => "p"

/** Item types; all print as <code>#</code> in the ASCII view. */
enum ItemClasses:
  case Halberd
  case Staff
  case Axe
  case BigPotion
  case SmallPotion
  case Dust
  case Sign
  case ExitDoor

  override def toString: String = "#"

/** High-level controller states handled by the GameEngine. */
enum GameControllerState:
  case Menu
  case ChampSelect
  case GameRunning
  case GameOver

object Direction:
  private val nameMap: Map[String, Direction] =
    Map(
      "n" -> North,
      "north" -> North,
      "s" -> South,
      "south" -> South,
      "w" -> West,
      "west" -> West,
      "e" -> East,
      "east" -> East
    )

  private val wasdMap: Map[String, Direction] =
    Map(
      "w" -> North,
      "s" -> South,
      "a" -> West,
      "d" -> East
    )

  def fromString(s: String): Option[Direction] =
    nameMap.get(s.toLowerCase)

  def fromStringWASD(s: String): Option[Direction] =
    wasdMap.get(s.toLowerCase)

/** Cardinal directions plus helpers. */
enum Direction(val dx: Int, val dy: Int):
  case North extends Direction(0, -1)
  case South extends Direction(0, 1)
  case West extends Direction(-1, 0)
  case East extends Direction(1, 0)

  def opposite: Direction = this match
    case Direction.North => Direction.South
    case Direction.South => Direction.North
    case Direction.West  => Direction.East
    case Direction.East  => Direction.West

  def vector: Point2D = Point2D(dx, dy)

  def doorMat: Point2D = Point2D(-dx, -dy)

/** ADT for every user command parsed from the view layer. */
sealed trait PlayerCommand

object PlayerCommand:
  case class Movement(direction: Direction) extends PlayerCommand

  case class Attack(direction: Direction) extends PlayerCommand

  case class Use(itemName: String) extends PlayerCommand

  case class Interact(direction: Direction) extends PlayerCommand

  case object Quit extends PlayerCommand

  case object Help extends PlayerCommand

  case object Undo extends PlayerCommand
