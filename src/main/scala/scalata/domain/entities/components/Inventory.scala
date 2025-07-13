package scalata.domain.entities.components

import scalata.domain.entities.Item
import scalata.domain.entities.items.Weapon

trait Inventory[E <: Inventory[E]]:
  val inventory: List[Item]
  val weapon: Option[Weapon]

  def equipWeapon(weapon: Weapon): E

  def addItem(item: Item): E

  def getItem(itemName: String): Option[Item]

  def removeItem(item: Item): E
