package scalata.domain.util

/** Enumeration of domain-level errors exchanged between
 * use-cases, controllers and the view layer.
 *
 * <h4>Contract</h4>
 * Each error provides:
 * <ul>
 * <li><code>message</code> – short, end-user text shown in the UI.</li>
 * <li><code>errorCode</code> – stable identifier useful for logs or tests.</li>
 * </ul>
 */
sealed trait GameError:
  def message: String

  def errorCode: String

object GameError:

  case class Help() extends GameError:
    val message: String = "?"
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

  case class UndoError() extends GameError:
    val message: String = "State already at the root"
    val errorCode = "UNDO_ERROR"
