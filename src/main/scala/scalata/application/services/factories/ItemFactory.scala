package scalata.application.services.factories

import scalata.application.services.EntityFactory
import scalata.domain.entities.Item
import scalata.domain.entities.items.{Dust, Potion, Weapon}
import scalata.domain.util.{ItemClasses, POTION_WEIGHT, gaussianBetween, weightedRandom}

import scala.util.Random

class ItemFactory extends EntityFactory[ItemFactory, Item, ItemClasses]:

  def createBox(difficulty: Int): Item =
    if weightedRandom(POTION_WEIGHT) then createPotion(difficulty)
    else createWeaponBox(difficulty)

  private def createPotion(difficulty: Int): Item =
    gaussianBetween(1.0, 2.0, difficulty) match
      case 1 => create(ItemClasses.SmallPotion)
      case 2 => create(ItemClasses.BigPotion)

  private def createWeaponBox(difficulty: Int): Item =
    Random.nextGaussian().round.toInt.max(0).min(1) match
      case 0 => create(ItemClasses.Dust)
      case 1 => createWeapon(difficulty)

  private def createWeapon(difficulty: Int): Item =
   gaussianBetween(1.0, 3.0, difficulty) match
     case 1 => create(ItemClasses.Axe)
     case 2 => create(ItemClasses.Staff)
     case 3 => create(ItemClasses.Halberd)

  override def create(entityType: ItemClasses): Item =
    entityType match
      case ItemClasses.Halberd =>
        Weapon(
          position = None,
          name = "Halberd",
          damage = 15,
          itemClass = ItemClasses.Halberd
        )
      case ItemClasses.Staff =>
        Weapon(
          position = None,
          name = "Staff",
          damage = 10,
          itemClass = ItemClasses.Staff
        )
      case ItemClasses.Axe =>
        Weapon(
          position = None,
          name = "Axe",
          damage = 7,
          itemClass = ItemClasses.Axe
        )
      case ItemClasses.BigPotion =>
        Potion(
          position = None,
          name = "BigPotion",
          itemClass = ItemClasses.BigPotion,
          amount = 40
        )
      case ItemClasses.SmallPotion =>
        Potion(
          position = None,
          name = "SmallPotion",
          itemClass = ItemClasses.SmallPotion,
          amount = 20
        )
      case ItemClasses.Dust =>
        Dust(
          position = None
        )
