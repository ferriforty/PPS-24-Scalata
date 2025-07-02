package scalata.domain.util

import scalata.domain.util.Geometry.Point2D

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
      case PlayerClasses.Mage      => 5
      case PlayerClasses.Barbarian => 6
      case PlayerClasses.Assassin  => 3

  override def toString: String = "@"

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

enum GameControllerState:
  case Menu
  case ChampSelect
  case GameRunning
  case GameOver

object Direction:
  def fromString(s: String): Option[Direction] = s.toLowerCase match
    case "n" | "north" => Some(Direction.North)
    case "s" | "south" => Some(Direction.South)
    case "w" | "west"  => Some(Direction.West)
    case "e" | "east"  => Some(Direction.East)
    case _             => None

  def fromStringMovement(s: String): Option[Direction] = s.toLowerCase match
    case "w" => Some(Direction.North)
    case "s" => Some(Direction.South)
    case "d" => Some(Direction.East)
    case "a" => Some(Direction.West)
    case _ => None

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
      
  def pointsTo: Point2D =
    this match
      case Direction.North => Point2D(0, -1)
      case Direction.South => Point2D(0, 1)
      case Direction.West  => Point2D(-1, 0)
      case Direction.East  => Point2D(1, 0)

  def doorMat: Point2D =
    this match
      case Direction.North => Point2D(0, 1)
      case Direction.South => Point2D(0, -1)
      case Direction.West  => Point2D(1, 0)
      case Direction.East  => Point2D(-1, 0)

sealed trait PlayerCommand
object PlayerCommand:
  case class Movement(direction: Direction) extends PlayerCommand
  case class Attack(direction: Direction) extends PlayerCommand
  case class Use(itemName: String) extends PlayerCommand
  case class Interact(direction: Direction) extends PlayerCommand
  case object Quit extends PlayerCommand