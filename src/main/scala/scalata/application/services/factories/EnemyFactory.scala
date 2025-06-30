package scalata.application.services.factories

import scalata.application.services.EntityFactory
import scalata.domain.entities.Enemy
import scalata.domain.util.EnemyClasses

import scala.util.Random

class EnemyFactory extends EntityFactory[EnemyFactory, Enemy, EnemyClasses]:
  def randomGeneration: Enemy =
    create(EnemyClasses.values(Random.nextInt(EnemyClasses.values.length)))

  override def create(entityType: EnemyClasses): Enemy =
    entityType match
      case EnemyClasses.Goblin =>
        Enemy(
          name = "Goblin",
          enemyType = entityType,
          health = 40,
          maxHealth = 40,
          attackPower = 10
        )
      case EnemyClasses.Pig =>
        Enemy(
          name = "Pig",
          enemyType = entityType,
          health = 60,
          maxHealth = 60,
          attackPower = 5
        )
