package scalata.domain.entities.items

import scalata.domain.entities.components.{Pickable, Usable}
import scalata.domain.entities.{Item, Player}
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.{GameResult, ItemClasses}
import scalata.domain.world.GameSession

/** Consumable healing potion.
 *
 * <ul>
 * <li><b>amount</b> points healed when used.</li>
 * <li><b>Pickable</b> calling <code>interact</code> picks up the potion.</li>
 * <li><b>Usable</b> consuming the potion heals the player and removes it from the inventory.</li>
 * </ul>
 */
final case class Potion(
    override val id: String,
    override val position: Option[Point2D] = None,
    override val name: String,
    override val itemClass: ItemClasses,
    amount: Int
) extends Item
    with Pickable:

  private def setPosition(pos: Option[Point2D]): Potion = copy(position = pos)

  override def interact(gameSession: GameSession): GameResult[GameSession] =
    GameResult.success(pick(this, gameSession))

  /** @inheritdoc */
  override def spawn(pos: Option[Point2D]): Potion = setPosition(pos)

/** Companion with [[Usable]] instance logic. */
object Potion:
  given Usable[Potion, Player] with
    def use(p: Potion, owner: Player): Player =
      owner.heal(p.amount).removeItem(p)
