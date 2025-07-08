package scalata.application.services.factories

import scalata.application.services.EntityFactory
import scalata.domain.entities.Enemy
import scalata.domain.util.EnemyClasses

import scala.util.Random

class EnemyFactory extends EntityFactory[EnemyFactory, Enemy, EnemyClasses]:
  def randomGeneration(id: String): Enemy =
    create(EnemyClasses.values(Random.nextInt(EnemyClasses.values.length)), id)

  override def create(entityType: EnemyClasses, id: String): Enemy =
    entityType match
      case EnemyClasses.Goblin =>
        Enemy(
          id = id,
          name = "Goblin",
          enemyType = entityType,
          health = 40,
          maxHealth = 40,
          attackPower = 10
        )
      case EnemyClasses.Pig =>
        Enemy(
          id = id,
          name = "Pig",
          enemyType = entityType,
          health = 60,
          maxHealth = 60,
          attackPower = 5
        )
