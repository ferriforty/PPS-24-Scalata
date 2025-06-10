package scalata.domain.util

enum GameControllerState:
  case Menu
  case ChampSelect
  case GameRunning
  case GameOver

enum Direction:
  case North
  case South
  case West
  case East

  def getOpposite: Direction =
    this match
      case North => South
      case South => North
      case West => East
      case East => West

object Direction:
  def fromString(s: String): Option[Direction] = s.toLowerCase match
    case "n" | "north" => Some(North)
    case "s" | "south" => Some(South)
    case "w" | "west" => Some(West)
    case "e" | "east" => Some(East)
    case _ => None