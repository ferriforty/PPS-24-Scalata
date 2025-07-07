package scalata.application.usecases.enemyusecase

import scalata.application.usecases.CreatureUseCase
import scalata.domain.entities.Player
import scalata.domain.util.{Direction, GameResult}
import scalata.domain.world.GameSession

class EnemyAttackUseCase extends CreatureUseCase[Player, Direction]:
  override def execute(_param: Direction, gameSession: GameSession): Player =
    val currentRoom = gameSession.getWorld
      .getRoom(gameSession.getGameState.currentRoom)
      .getOrElse(
        throw IllegalStateException("Room Not defined in Interact use case")
      )
    val player = gameSession.getWorld.getPlayer

    currentRoom.getAliveEnemies
      .filter: e =>
        e.position
          .neighboursFiltered(currentRoom.isInside)
          .contains(player.position)
      .foldLeft(player): (p, e) =>
        e.attack(p)

