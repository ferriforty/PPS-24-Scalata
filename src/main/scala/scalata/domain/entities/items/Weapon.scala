package scalata.domain.entities.items

import scalata.domain.entities.components.{Pickable, Usable}
import scalata.domain.entities.{Item, Player}
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.{GameResult, ItemClasses}
import scalata.domain.world.GameSession

/** Equippable weapon.
  *
  * <h4>Key points</h4> <ul> <li><b>damage</b> – base attack value added to the
  * player’s own power.</li> <li><b>Pickable</b> – calling <code>interact</code>
  * removes the item from the floor and places it in the player inventory.</li>
  * <li><b>Usable</b> – the given instance in the companion equips the weapon
  * and then deletes it from the bag (one-slot logic).</li> </ul>
  */
final case class Weapon(
    override val id: String,
    override val position: Option[Point2D] = None,
    override val name: String,
    override val itemClass: ItemClasses,
    damage: Int
) extends Item
    with Pickable:

  private def setPosition(pos: Option[Point2D]): Weapon = copy(position = pos)

  /** Pick up the weapon. */
  override def interact(gameSession: GameSession): GameResult[GameSession] =
    GameResult.success(pick(this, gameSession))

  /** @inheritdoc */
  override def spawn(pos: Option[Point2D]): Weapon = setPosition(pos)

  /** Compute total damage dealt when used by a player. */
  def attack(playerAttack: Int): Int = this.damage + playerAttack

/** Companion utilities and <code>Usable</code> instance. */
object Weapon:
  given Usable[Weapon, Player] with
    def use(w: Weapon, owner: Player): Player =
      owner.equipWeapon(weapon = w).removeItem(w)
