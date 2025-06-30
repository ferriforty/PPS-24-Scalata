package scalata.domain.entities

import scalata.domain.entities.components.Interactable
import scalata.domain.util.Point2D

trait Item extends Entity with Interactable:
  val position: Option[Point2D]

  def isPicked: Boolean = this.position.isEmpty
  def spawn(pos: Option[Point2D]): Item
  def use(entity: Entity): Entity = entity
  override def toString: String = "$"
