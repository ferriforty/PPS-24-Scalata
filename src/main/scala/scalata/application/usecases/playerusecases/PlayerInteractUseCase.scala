package scalata.application.usecases.playerusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.util.{Direction, GameError, GameResult}
import scalata.domain.world.GameSession

class PlayerInteractUseCase
    extends CreatureUseCase[GameResult[
      GameSession
    ], Direction]:
  override def execute(
      param: Direction,
      gameSession: GameSession
  ): GameResult[GameSession] =

    val currentRoom = gameSession.getWorld
      .getRoom(gameSession.getGameState.currentRoom)
      .getOrElse(
        throw IllegalStateException("Room Not defined in Interact use case")
      )

    val itemPos = gameSession.getWorld.player.position.moveBy(param.vector)
    currentRoom
      .getItemAtPosition(itemPos)
      .fold(
        GameResult.error(GameError.ItemNotPresent())
      )(item => item.interact(gameSession))
