package scalata.domain.entities.items

import scalata.domain.entities.components.{Pickable, Usable}
import scalata.domain.entities.{Entity, Item, Player}
import scalata.domain.util.{GameResult, ItemClasses}
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

  override def interact(gameSession: GameSession): GameResult[GameSession] =
    GameResult.success(pick(this, gameSession))

  override def spawn(pos: Option[Point2D]): Weapon = setPosition(pos)

  def attack(playerAttack: Int): Int = this.damage + playerAttack

object Weapon:
  given Usable[Weapon, Player] with
    def use(w: Weapon, owner: Player): Player =
      owner.equipWeapon(weapon = w).removeItem(w)
