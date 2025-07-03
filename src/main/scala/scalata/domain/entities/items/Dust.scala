package scalata.domain.entities.items

import scalata.domain.entities.components.{Pickable, Usable}
import scalata.domain.entities.{Entity, Item, Player}
import scalata.domain.util.{GameResult, ItemClasses}
import scalata.domain.util.Geometry.Point2D
import scalata.domain.world.GameSession

final case class Dust(
    override val position: Option[Point2D] = None,
    override val name: String = "Dust",
    override val itemClass: ItemClasses = ItemClasses.Dust
) extends Item
    with Pickable:

  private def setPosition(pos: Option[Point2D]): Dust = copy(position = pos)

  override def interact(gameSession: GameSession): GameResult[GameSession] =
    GameResult.success(pick(this, gameSession))

  override def spawn(pos: Option[Point2D]): Dust = setPosition(pos)

object Dust:
  given Usable[Dust, Player] with
    def use(d: Dust, owner: Player): Player =
      owner.removeItem(d)
