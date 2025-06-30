package scalata.domain.entities.components

trait Alive[E <: Alive[E]]:
  val health: Int
  val maxHealth: Int
  def takeDamage(damage: Int): E
  def heal(amount: Int): E
  def isAlive: Boolean
