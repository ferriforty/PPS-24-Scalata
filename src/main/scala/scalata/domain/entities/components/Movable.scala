package scalata.domain.entities.components

import scalata.domain.util.Geometry.Point2D

/** Capability mix-in for entities that can change their position.
 *
 * <h4>Contract</h4>
 * <ul>
 * <li><b>position</b> – current tile coordinate.</li>
 * <li><b>move</b> – returns a <em>new</em> instance at a different
 * <code>Point2D</code>; never mutates in place.</li>
 * </ul>
 *
 * @tparam E concrete entity type that implements the trait
 */
trait Movable[E <: Movable[E]]:
  val position: Point2D

  /** Produce a copy of the entity at <code>pos</code>. */
  def move(pos: Point2D): E
