package scalata.domain.entities.components

trait Combatant[E]:
  val attackPower: Int

  def attack(opponent: E): E
