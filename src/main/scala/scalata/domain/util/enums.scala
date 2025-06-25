package scalata.domain.util

sealed trait PlayerClasses:
  override def toString: String = "@"

  def reach: Int =
    this match
      case PlayerClasses.Mage => 2
      case PlayerClasses.Barbarian => 1
      case PlayerClasses.Assassin => 1

  def visibility: Int =
    this match
      case PlayerClasses.Mage => 5
      case PlayerClasses.Barbarian => 6
      case PlayerClasses.Assassin => 3
  
object PlayerClasses:
  case object Mage extends PlayerClasses
  case object Barbarian extends PlayerClasses
  case object Assassin extends PlayerClasses

sealed trait GameControllerState
object GameControllerState:
  case object Menu extends GameControllerState
  case object ChampSelect extends GameControllerState
  case object GameRunning extends GameControllerState
  case object GameOver extends GameControllerState

sealed trait Direction:
  def getOpposite: Direction =
    this match
      case Direction.North => Direction.South
      case Direction.South => Direction.North
      case Direction.West  => Direction.East
      case Direction.East  => Direction.West

  def fromString(s: String): Option[Direction] = s.toLowerCase match
    case "n" | "north" => Some(Direction.North)
    case "s" | "south" => Some(Direction.South)
    case "w" | "west"  => Some(Direction.West)
    case "e" | "east"  => Some(Direction.East)
    case _             => None

object Direction:
  case object North extends Direction
  case object South extends Direction
  case object West extends Direction
  case object East extends Direction
