package scalata.domain.entities

import scalata.domain.entities.components.{Alive, Movable}
import scalata.domain.util.{PlayerClasses, Point2D}

final case class Player(
    name: String = "Hero",
    role: PlayerClasses,
    health: Int,
    maxHealth: Int,
    position: Point2D = Point2D(0, 0)
) extends Entity
    with Movable[Player]
    with Alive[Player]:

  override def move(pos: Point2D): Player = copy(position = pos)

  override def takeDamage(damage: Int): Player =
    copy(health = (health - damage).max(0))

  override def heal(amount: Int): Player =
    copy(health = (health + amount).min(maxHealth))

  override def isAlive: Boolean = health > 0
