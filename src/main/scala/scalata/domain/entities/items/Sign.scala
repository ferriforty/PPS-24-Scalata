package scalata.domain.entities.items

import scalata.domain.entities.Item
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.{GameResult, ItemClasses}
import scalata.domain.world.GameSession

/** Floor decoration that displays the current tower level when the
 * player interacts with it.
 */
final case class Sign(
    override val id: String,
    override val position: Option[Point2D] = None,
    override val name: String = "Sign",
    override val itemClass: ItemClasses = ItemClasses.Sign
) extends Item:

  private def setPosition(pos: Option[Point2D]): Sign = copy(position = pos)

  /** Display the current level without modifying the session. */
  override def interact(gameSession: GameSession): GameResult[GameSession] =
    GameResult.success(
      value = gameSession,
      message = Some("Current level: " + gameSession.getGameState.currentLevel)
    )

  /** @inheritdoc */
  override def spawn(pos: Option[Point2D]): Sign = setPosition(pos)
