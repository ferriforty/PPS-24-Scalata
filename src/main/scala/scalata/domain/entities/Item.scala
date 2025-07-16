package scalata.domain.entities

import scalata.domain.entities.components.Interactable
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.ItemClasses

/** Base trait for every pick-able game object (potions, weapons, etc.). */
trait Item extends Entity with Interactable:
  val position: Option[Point2D]
  val itemClass: ItemClasses

  /** <code>true</code> if the item has been removed from the floor. */
  def isPicked: Boolean = this.position.isEmpty

  /** Create the same item at a given position. */
  def spawn(pos: Option[Point2D]): Item

  /** Glyph used by the ASCII renderer. */
  override def toString: String = "$"
