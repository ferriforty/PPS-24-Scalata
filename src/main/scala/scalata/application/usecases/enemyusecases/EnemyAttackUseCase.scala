package scalata.application.usecases.enemyusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.entities.Player
import scalata.domain.world.{GameSession, Room}

class EnemyAttackUseCase extends CreatureUseCase[Player, Room]:
  override def execute(currentRoom: Room, gameSession: GameSession): Player =
    val player = gameSession.getWorld.getPlayer

    currentRoom.getAliveEnemies
      .filter: e =>
        e.position
          .neighboursFiltered(currentRoom.isInside)
          .contains(player.position)
      .foldLeft(player): (p, e) =>
        e.attack(p)
