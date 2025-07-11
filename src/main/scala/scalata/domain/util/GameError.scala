package scalata.domain.util

sealed trait GameError:
  def message: String
  def errorCode: String

object GameError:

  case class Help() extends GameError:
    val message: String = "\n" +
      """[W/A/S/D]  Move
        |[A]        Attack (choose direction: [N/S/E/W])
        |[I]        Interact (choose direction: [N/S/E/W])
        |[U]        Use item (specify item name)
        |[Q]        Quit""".stripMargin + "\n"
    val errorCode = "HELP"

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

  case class Undo() extends GameError:
    val message: String = "Undo"
    val errorCode = "UNDO"