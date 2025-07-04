package scalata.domain.util

sealed trait GameError:
  def message: String
  def errorCode: String

object GameError:
  case class InvalidInput(input: String) extends GameError:
    val message: String = "Invalid Input " + input
    val errorCode = "INVALID_INPUT"

  case class GameOver() extends GameError:
    val message: String = "Game Over"
    val errorCode = "GAME_OVER"
