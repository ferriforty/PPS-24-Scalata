package scalata.domain.entities.components

trait Alive[E <: Movable[E]]:
  def takeDamage(damage: Int): E
  def heal(amount: Int): E
  def isAlive: Boolean
