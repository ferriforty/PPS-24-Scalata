package scalata.domain.entities.components

import scalata.domain.entities.Item
import scalata.domain.entities.items.Weapon

/** Capability mix-in for entities that own a inventory plus an optional
  * equipped weapon.
  *
  * <ul> <li><b>inventory</b> – list of items currently held.</li>
  * <li><b>weapon</b> – optional equipped [[Weapon]].</li>
  * <li><b>equipWeapon</b> – returns a new instance with the weapon set.</li>
  * <li><b>addItem</b> – adds an item to the inventory.</li> <li><b>getItem</b>
  * – finds an item by (trimmed, case-insensitive) name.</li>
  * <li><b>removeItem</b> – deletes an item from the inventory.</li> </ul>
  *
  * @tparam E
  *   concrete entity type that mixes in <code>Inventory</code>
  */
trait Inventory[E <: Inventory[E]]:
  val inventory: List[Item]
  val weapon: Option[Weapon]

  def equipWeapon(weapon: Weapon): E

  def addItem(item: Item): E

  def getItem(itemName: String): Option[Item]

  def removeItem(item: Item): E
