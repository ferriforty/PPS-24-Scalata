package scalata.domain.entities

import scalata.domain.entities.components.Movable
import scalata.domain.util.{PlayerClasses, Point2D}

final case class Player(
    name: String = "Hero",
    role: PlayerClasses,
    position: Point2D = Point2D(0, 0)
) extends Entity
    with Movable:

  override def move(pos: Point2D): Player = copy(position = pos)
