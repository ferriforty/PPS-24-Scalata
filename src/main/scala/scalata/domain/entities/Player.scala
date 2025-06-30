package scalata.domain.entities

import scalata.domain.entities.components.{Alive, Combatant, Movable}
import scalata.domain.util.{PlayerClasses, Point2D}

final case class Player(
    override val name: String = "Hero",
    role: PlayerClasses,
    override val health: Int,
    override val maxHealth: Int,
    override val position: Point2D = Point2D(0, 0),
    override val attackPower: Int
) extends Entity
    with Movable[Player]
    with Alive[Player]
    with Combatant[Enemy]:

  override def move(pos: Point2D): Player = copy(position = pos)

  override def takeDamage(damage: Int): Player =
    copy(health = (health - damage).max(0))

  override def heal(amount: Int): Player =
    copy(health = (health + amount).min(maxHealth))

  override def isAlive: Boolean = health > 0

  override def attack(opponent: Enemy): Enemy =
    opponent.takeDamage(attackPower)
