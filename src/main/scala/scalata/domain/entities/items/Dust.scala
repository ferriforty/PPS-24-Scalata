package scalata.domain.entities.items

import scalata.domain.entities.components.{Pickable, Usable}
import scalata.domain.entities.{Item, Player}
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.{GameResult, ItemClasses}
import scalata.domain.world.GameSession

/** Small, throw-away item with no direct effect.
  *
  * <ul> <li><b>Usable</b> – using it simply discards the item (flavour
  * only).</li> </ul>
  */
final case class Dust(
    override val id: String,
    override val position: Option[Point2D] = None,
    override val name: String = "Dust",
    override val itemClass: ItemClasses = ItemClasses.Dust
) extends Item
    with Pickable:

  private def setPosition(pos: Option[Point2D]): Dust = copy(position = pos)

  override def interact(gameSession: GameSession): GameResult[GameSession] =
    GameResult.success(pick(this, gameSession))

  /** @inheritdoc */
  override def spawn(pos: Option[Point2D]): Dust = setPosition(pos)

/** Companion containing the [[Usable]] instance. */
object Dust:
  given Usable[Dust, Player] with
    def use(d: Dust, owner: Player): Player =
      owner.removeItem(d)
