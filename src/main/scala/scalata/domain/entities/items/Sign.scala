package scalata.domain.entities.items

import scalata.domain.entities.Item
import scalata.domain.util.{GameResult, ItemClasses}
import scalata.domain.util.Geometry.Point2D
import scalata.domain.world.GameSession

final case class Sign(
    override val position: Option[Point2D] = None,
    override val name: String = "Sign",
    override val itemClass: ItemClasses = ItemClasses.Sign
) extends Item:

  private def setPosition(pos: Option[Point2D]): Sign = copy(position = pos)

  override def interact(gameSession: GameSession): GameResult[GameSession] =
    GameResult.success(
      value = gameSession,
      message = Some("Current level: " + gameSession.getGameState.currentLevel)
    )

  override def spawn(pos: Option[Point2D]): Sign = setPosition(pos)
