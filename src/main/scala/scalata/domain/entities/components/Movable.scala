package scalata.domain.entities.components

import scalata.domain.entities.Entity
import scalata.domain.util.Point2D

trait Movable[E <: Movable[E]]:
  def move(pos: Point2D): E
