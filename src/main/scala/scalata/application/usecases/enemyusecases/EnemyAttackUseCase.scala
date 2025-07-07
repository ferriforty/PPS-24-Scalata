package scalata.application.usecases.enemyusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.entities.Player
import scalata.domain.world.{GameSession, Room}

class EnemyAttackUseCase extends CreatureUseCase[Player, Room]:
  override def execute(param: Room, gameSession: GameSession): Player =
    val player = gameSession.getWorld.getPlayer

    param.getAliveEnemies
      .filter: e =>
        e.position
          .neighboursFiltered(param.isInside)
          .contains(player.position)
      .foldLeft(player): (p, e) =>
        e.attack(p)

