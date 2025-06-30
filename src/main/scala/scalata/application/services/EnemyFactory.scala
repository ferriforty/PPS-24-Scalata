package scalata.application.services

import scalata.domain.entities.Enemy
import scalata.domain.util.EnemyClasses

class EnemyFactory extends EntityFactory[EnemyFactory, Enemy, EnemyClasses]:
  override def create(entityType: EnemyClasses): Enemy = ???
