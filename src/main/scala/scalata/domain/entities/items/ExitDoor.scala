package scalata.domain.entities.items

import scalata.domain.entities.components.Pickable
import scalata.domain.entities.{Entity, Item, Player}
import scalata.domain.util.ItemClasses
import scalata.domain.util.Geometry.Point2D
import scalata.domain.world.GameSession

final case class ExitDoor(
    override val position: Option[Point2D] = None,
    override val name: String = "Exit Door",
    override val itemClass: ItemClasses = ItemClasses.ExitDoor
) extends Item:

  private def setPosition(pos: Option[Point2D]): ExitDoor = copy(position = pos)

  override def interact(gameSession: GameSession): GameSession = ???

  override def spawn(pos: Option[Point2D]): ExitDoor = setPosition(pos)
