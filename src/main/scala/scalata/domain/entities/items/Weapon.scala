package scalata.domain.entities.items

import scalata.domain.entities.components.Pickable
import scalata.domain.entities.{Entity, Item, Player}
import scalata.domain.util.ItemClasses
import scalata.domain.util.Geometry.Point2D
import scalata.domain.world.GameSession

final case class Weapon(
    override val position: Option[Point2D] = None,
    override val name: String,
    override val itemClass: ItemClasses,
    damage: Int
) extends Item
    with Pickable:

  private def setPosition(pos: Option[Point2D]): Weapon = copy(position = pos)

  override def interact(gameSession: GameSession): GameSession =
    pick(this, gameSession)

  override def spawn(pos: Option[Point2D]): Weapon = setPosition(pos)

  override def use(entity: Entity): Entity =
    entity match
      case p: Player => p.equipWeapon(weapon = this).removeItem(this)
      case _         => entity

  def attack(playerAttack: Int): Int = this.damage + playerAttack
