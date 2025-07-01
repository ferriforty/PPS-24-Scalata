package scalata.domain.entities.items

import scalata.domain.entities.components.Pickable
import scalata.domain.entities.{Enemy, Entity, Item, Player}
import scalata.domain.util.{ItemClasses, Point2D}
import scalata.domain.world.GameSession

final case class Potion(
    override val position: Option[Point2D] = None,
    override val name: String,
    override val itemClass: ItemClasses,
    amount: Int
) extends Item
    with Pickable:

  private def setPosition(pos: Option[Point2D]): Potion = copy(position = pos)

  override def interact(gameSession: GameSession): GameSession =
    pick(this.setPosition(None), gameSession)

  override def spawn(pos: Option[Point2D]): Potion = setPosition(pos)

  override def use(entity: Entity): Entity =
    entity match
      case p: Player => p.heal(amount).removeItem(this)
      case _         => entity
      
