package scalata.domain.entities

import scalata.domain.entities.components.{Alive, Combatant, Movable}
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.EnemyClasses

final case class Enemy(
    override val id: String,
    override val name: String,
    enemyType: EnemyClasses,
    override val position: Point2D = Point2D(0, 0),
    override val health: Int,
    override val maxHealth: Int,
    override val attackPower: Int
) extends Entity
    with Alive[Enemy]
    with Movable[Enemy]
    with Combatant[Player]:

  override def takeDamage(damage: Int): Enemy =
    copy(health = (health - damage).max(0))

  override def heal(amount: Int): Enemy =
    copy(health = (health + amount).min(maxHealth))

  override def isAlive: Boolean = health > 0

  override def move(pos: Point2D): Enemy = copy(position = pos)

  override def attack(opponent: Player): Player =
    opponent.takeDamage(attackPower)
