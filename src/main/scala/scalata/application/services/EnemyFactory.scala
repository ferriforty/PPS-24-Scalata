package scalata.application.services

import scalata.domain.entities.Enemy
import scalata.domain.util.EnemyClasses

class EnemyFactory extends EntityFactory[EnemyFactory, Enemy, EnemyClasses]:
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
