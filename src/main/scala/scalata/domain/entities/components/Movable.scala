package scalata.domain.entities.components

import scalata.domain.util.Point2D

trait Movable[E <: Movable[E]]:
  val position: Point2D
  def move(pos: Point2D): E
