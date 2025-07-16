package scalata.domain.entities.components

/** Capability mix-in for entities that possess hit-points.
  *
  * @tparam E
  *   concrete entity type that mixes in <code>Alive</code>
  */
trait Alive[E <: Alive[E]]:
  val health: Int
  val maxHealth: Int

  /** Apply damage and return the updated entity. */
  def takeDamage(damage: Int): E

  /** Heal the entity (never exceeds <code>maxHealth</code>). */
  def heal(amount: Int): E

  /** <code>true</code> when <code>health &gt; 0</code>. */
  def isAlive: Boolean
