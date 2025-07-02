package scalata.domain.util

sealed trait GameError:
  def message: String
  def errorCode: String

object GameError:

  case class ItemNotOwned() extends GameError:
    val message: String = "You don't own this item."
    val errorCode = "ITEM_NOT_OWNED"

  case class ItemNotPresent() extends GameError:
    val message: String = "item not present there"
    val errorCode = "ITEM_NOT_PRESENT"

  case class InvalidDirection() extends GameError:
    val message: String = "can't go this way"
    val errorCode = "INVALID_POSITION"

  case class InvalidInput(input: String) extends GameError:
    val message: String = "Invalid Input " + input
    val errorCode = "INVALID_INPUT"

  case class GameOver() extends GameError:
    val message: String = "Game Over"
    val errorCode = "GAME_OVER"
