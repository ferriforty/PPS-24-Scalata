package scalata.application.usecases.playerusecases

import scalata.application.usecases.PlayerUseCase
import scalata.domain.util.{Direction, GameResult, Point2D}
import scalata.domain.world.GameSession

class PlayerAttackUseCase extends PlayerUseCase[PlayerAttackUseCase, GameResult[GameSession], Direction]:
  override def execute(param: Direction, gameSession: GameSession): GameResult[GameSession] =
    val player = gameSession.getWorld.player
    val currentRoom = gameSession.getWorld
      .getRoom(gameSession.getGameState.currentRoom)
      .getOrElse(
        throw new IllegalStateException(
          "Room Not defined in attack use case"
        )
      )
    val reachVector = param match
      case Direction.North => Point2D(0, -player.reach())
      case Direction.South => Point2D(0, player.reach())
      case Direction.West => Point2D(-player.reach(), 0)
      case Direction.East => Point2D(player.reach(), 0)

    val attackLine = player.position.rangeTo(
      player.position.moveBy(reachVector)
    )
    
    GameResult.success(
      gameSession.updateWorld(
        gameSession.getWorld.updateRoom(
          currentRoom.withEnemies(
            currentRoom.enemies.map: e =>
              if e.isAlive && attackLine.contains(e.position) then
                player.attack(e)
              else e
          )
        )
      )
    )