package scalata.domain.entities

import scalata.domain.entities.components.{Alive, Combatant, Inventory, Movable}
import scalata.domain.entities.items.Weapon
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.PlayerClasses

final case class Player(
    override val id: String,
    override val name: String = "Hero",
    role: PlayerClasses,
    override val health: Int,
    override val maxHealth: Int,
    override val position: Point2D = Point2D(0, 0),
    override val weapon: Option[Weapon] = None,
    override val inventory: List[Item] = List.empty,
    override val attackPower: Int
) extends Entity
    with Movable[Player]
    with Alive[Player]
    with Combatant[Enemy]
    with Inventory[Player]:

  override def move(pos: Point2D): Player = copy(position = pos)

  override def takeDamage(damage: Int): Player =
    copy(health = (health - damage).max(0))

  override def heal(amount: Int): Player =
    copy(health = (health + amount).min(maxHealth))

  override def isAlive: Boolean = health > 0

  override def attack(opponent: Enemy): Enemy =
    weapon match
      case Some(w) => opponent.takeDamage(w.attack(attackPower))
      case None    => opponent.takeDamage(attackPower)

  override def equipWeapon(weapon: Weapon): Player =
    copy(weapon = Some(weapon))

  override def addItem(item: Item): Player =
    copy(inventory = inventory :+ item)

  override def getItem(itemName: String): Option[Item] =
    this.inventory.find(i =>
      i.name.replaceAll("\\s", "").toLowerCase == itemName
    )

  override def removeItem(item: Item): Player =
    copy(inventory = inventory.filterNot(_ == item))

  def reach(): Integer = this.role.reach

  def visibility(): Integer = this.role.visibility

  def playerSymbol: String = "@"

  override def toString: String =
    s"$name: ${role.toString} (HP: $health/$maxHealth, ATK: $attackPower)"
