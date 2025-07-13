package scalata.application.services.factories

import scalata.application.services.EntityFactory
import scalata.domain.entities.Item
import scalata.domain.entities.items.*
import scalata.domain.util.{
  ItemClasses,
  POTION_WEIGHT,
  gaussianBetween,
  weightedRandom
}

import scala.util.Random

class ItemFactory extends EntityFactory[ItemFactory, Item, ItemClasses]:

  def createBox(difficulty: Int, id: String): Item =
    if weightedRandom(POTION_WEIGHT) then createPotion(difficulty, id)
    else createWeaponBox(difficulty, id)

  private def createPotion(difficulty: Int, id: String): Item =
    gaussianBetween(1.0, 2.0, difficulty) match
      case 1 => create(ItemClasses.SmallPotion, id)
      case 2 => create(ItemClasses.BigPotion, id)

  private def createWeaponBox(difficulty: Int, id: String): Item =
    Random.nextGaussian().round.toInt.max(0).min(1) match
      case 0 => create(ItemClasses.Dust, id)
      case 1 => createWeapon(difficulty, id)

  private def createWeapon(difficulty: Int, id: String): Item =
    gaussianBetween(1.0, 3.0, difficulty) match
      case 1 => create(ItemClasses.Axe, id)
      case 2 => create(ItemClasses.Staff, id)
      case 3 => create(ItemClasses.Halberd, id)

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
