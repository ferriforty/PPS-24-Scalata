package scalata.application.usecases.enemyusecase

import scalata.application.usecases.CreatureUseCase
import scalata.domain.util.{Direction, GameResult}
import scalata.domain.world.GameSession

class EnemyMovementUseCase extends CreatureUseCase[GameResult[GameSession], Direction]:
  override def execute(param: Direction, gameSession: GameSession): GameResult[GameSession] =
    GameResult.success(gameSession)
