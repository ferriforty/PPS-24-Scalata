package scalata.domain.util

sealed trait PlayerClasses:
  override def toString: String = "@"

  def reach: Int =
    this match
      case PlayerClasses.Mage      => 2
      case PlayerClasses.Barbarian => 1
      case PlayerClasses.Assassin  => 1

  def visibility: Int =
    this match
      case PlayerClasses.Mage      => 5
      case PlayerClasses.Barbarian => 6
      case PlayerClasses.Assassin  => 3

object PlayerClasses:
  case object Mage extends PlayerClasses
  case object Barbarian extends PlayerClasses
  case object Assassin extends PlayerClasses

enum EnemyClasses:
  case Goblin
  case Pig

  override def toString: String =
    this match
      case EnemyClasses.Goblin => "g"
      case EnemyClasses.Pig    => "p"

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

sealed trait GameControllerState
object GameControllerState:
  case object Menu extends GameControllerState
  case object ChampSelect extends GameControllerState
  case object GameRunning extends GameControllerState
  case object GameOver extends GameControllerState

object Direction:
  def fromString(s: String): Option[Direction] = s.toLowerCase match
    case "n" | "north" => Some(Direction.North)
    case "s" | "south" => Some(Direction.South)
    case "w" | "west"  => Some(Direction.West)
    case "e" | "east"  => Some(Direction.East)
    case _             => None

enum Direction:
  case North
  case South
  case West
  case East

  def getOpposite: Direction =
    this match
      case Direction.North => Direction.South
      case Direction.South => Direction.North
      case Direction.West  => Direction.East
      case Direction.East  => Direction.West

  def doorMat: Point2D =
    this match
      case Direction.North => Point2D(0, -1)
      case Direction.South => Point2D(0, 1)
      case Direction.West  => Point2D(1, 0)
      case Direction.East  => Point2D(-1, 0)
