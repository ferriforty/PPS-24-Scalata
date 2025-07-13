package scalata.domain.entities

import scalata.domain.entities.components.Interactable
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.ItemClasses

trait Item extends Entity with Interactable:
  val position: Option[Point2D]
  val itemClass: ItemClasses

  def isPicked: Boolean = this.position.isEmpty

  def spawn(pos: Option[Point2D]): Item

  override def toString: String = "$"
