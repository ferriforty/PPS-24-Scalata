package scalata.domain.entities.items

import scalata.domain.entities.Item
import scalata.domain.util.ItemClasses
import scalata.domain.util.Geometry.Point2D
import scalata.domain.world.GameSession
import scalata.infrastructure.cli.view.GameRunView

final case class Sign(
  override val position: Option[Point2D] = None,
  override val name: String = "Sign",
  override val itemClass: ItemClasses = ItemClasses.Sign
) extends Item:

  private def setPosition(pos: Option[Point2D]): Sign = copy(position = pos)

  override def interact(gameSession: GameSession): GameSession =
    GameRunView.display("Current Level: " + gameSession.getGameState.currentLevel)
    gameSession

  override def spawn(pos: Option[Point2D]): Sign = setPosition(pos)
