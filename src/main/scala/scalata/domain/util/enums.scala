package scalata.domain.util

enum GameControllerState:
  case Menu
  case ChampSelect
  case GameRunning
  case GameOver

sealed trait Direction:
  def getOpposite: Direction =
    this match
      case Direction.North => Direction.South
      case Direction.South => Direction.North
      case Direction.West => Direction.East
      case Direction.East => Direction.West

  def fromString(s: String): Option[Direction] = s.toLowerCase match
    case "n" | "north" => Some(Direction.North)
    case "s" | "south" => Some(Direction.South)
    case "w" | "west" => Some(Direction.West)
    case "e" | "east" => Some(Direction.East)
    case _ => None

object Direction:
  case object North extends Direction
  case object South extends Direction
  case object West extends Direction
  case object East extends Direction