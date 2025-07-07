package scalata.application.usecases.enemyusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.world.{GameSession, Room}

class EnemyMovementUseCase extends CreatureUseCase[Room, Room]:
  override def execute(param: Room, gameSession: GameSession): Room =
    param
