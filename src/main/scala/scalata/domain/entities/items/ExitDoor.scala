package scalata.domain.entities.items

import scalata.domain.entities.Item
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.{GameResult, ItemClasses}
import scalata.domain.world.GameSession

/** Door that moves the player to the next floor when interacted with.
  *
  * <ul> <li><b>interact</b> – increments <code>currentLevel</code> in the
  * session’s <code>GameState</code> and returns a successful
  * [[scalata.domain.util.GameResult]].</li>
  */
final case class ExitDoor(
    override val id: String,
    override val position: Option[Point2D] = None,
    override val name: String = "Exit Door",
    override val itemClass: ItemClasses = ItemClasses.ExitDoor
) extends Item:

  private def setPosition(pos: Option[Point2D]): ExitDoor = copy(position = pos)

  override def interact(gameSession: GameSession): GameResult[GameSession] =
    GameResult.success(
      gameSession.updateGameState(
        gameSession.getGameState.nextLevel
      )
    )

  /** @inheritdoc */
  override def spawn(pos: Option[Point2D]): ExitDoor = setPosition(pos)
