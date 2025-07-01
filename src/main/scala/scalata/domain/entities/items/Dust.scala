package scalata.domain.entities.items

import scalata.domain.entities.components.Pickable
import scalata.domain.entities.{Entity, Item, Player}
import scalata.domain.util.{ItemClasses, Point2D}
import scalata.domain.world.GameSession

final case class Dust(
    override val position: Option[Point2D],
    override val name: String = "Dust",
    override val itemClass: ItemClasses = ItemClasses.Dust
) extends Item
    with Pickable:

  private def setPosition(pos: Option[Point2D]): Dust = copy(position = pos)

  override def interact(gameSession: GameSession): GameSession =
    pick(this.setPosition(None), gameSession)

  override def spawn(pos: Option[Point2D]): Dust = setPosition(pos)

  override def use(entity: Entity): Entity = entity
