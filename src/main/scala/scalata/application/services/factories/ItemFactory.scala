package scalata.application.services.factories

import scalata.application.services.EntityFactory
import scalata.domain.entities.Item
import scalata.domain.entities.items.*
import scalata.domain.util.{
  DUST_WEIGHT,
  ItemClasses,
  POTION_WEIGHT,
  gaussianBetween,
  weightedRandom
}

/** Factory that produces every kind of in-game **item**.
  *
  * <h4>Responsibilities</h4> <ul> <li><b>Loot box generation</b> – `createBox`
  * decides at runtime whether the chest contains a potion, a weapon or plain
  * dust, using weighted randomness that scales with the <code>difficulty</code>
  * parameter.</li> <li><b>Direct creation</b> – `create` instantiates a
  * concrete item from the [[scalata.domain.util.ItemClasses]]
  * discriminator.</li> </ul>
  *
  * All methods are <em>pure</em>; they return fresh instances and never mutate
  * shared state.
  */
class ItemFactory extends EntityFactory[ItemFactory, Item, ItemClasses]:

  /** Create a random loot box whose contents depend on <code>difficulty</code>.
    *
    * <ul> <li>With probability <code>POTION_WEIGHT</code>, call
    * [[createPotion]] (healing item).</li> <li>Otherwise call
    * [[createWeaponBox]] (weapon or dust).</li> </ul>
    */
  def createBox(difficulty: Int, id: String): Item =
    if weightedRandom(POTION_WEIGHT) then createPotion(difficulty, id)
    else createWeaponBox(difficulty, id)

  private def createPotion(difficulty: Int, id: String): Item =
    gaussianBetween(1.0, 2.0, difficulty) match
      case 1 => create(ItemClasses.SmallPotion, id)
      case 2 => create(ItemClasses.BigPotion, id)

  private def createWeaponBox(difficulty: Int, id: String): Item =
    if weightedRandom(DUST_WEIGHT) then create(ItemClasses.Dust, id)
    else createWeapon(difficulty, id)

  private def createWeapon(difficulty: Int, id: String): Item =
    gaussianBetween(1.0, 3.0, difficulty) match
      case 1 => create(ItemClasses.Axe, id)
      case 2 => create(ItemClasses.Staff, id)
      case 3 => create(ItemClasses.Halberd, id)

  /** Build a single item given its [[ItemClasses]] tag.
    *
    * @param entityType
    *   enumeration value selecting the concrete item
    * @param id
    *   unique identifier
    */
  override def create(entityType: ItemClasses, id: String): Item =
    entityType match
      case ItemClasses.Halberd =>
        Weapon(
          id = id,
          name = "Halberd",
          damage = 15,
          itemClass = ItemClasses.Halberd
        )
      case ItemClasses.Staff =>
        Weapon(
          id = id,
          name = "Staff",
          damage = 10,
          itemClass = ItemClasses.Staff
        )
      case ItemClasses.Axe =>
        Weapon(
          id = id,
          name = "Axe",
          damage = 7,
          itemClass = ItemClasses.Axe
        )
      case ItemClasses.BigPotion =>
        Potion(
          id = id,
          name = "BigPotion",
          itemClass = ItemClasses.BigPotion,
          amount = 40
        )
      case ItemClasses.SmallPotion =>
        Potion(
          id = id,
          name = "SmallPotion",
          itemClass = ItemClasses.SmallPotion,
          amount = 20
        )
      case ItemClasses.Dust     => Dust(id = id)
      case ItemClasses.Sign     => Sign(id = id)
      case ItemClasses.ExitDoor => ExitDoor(id = id)
