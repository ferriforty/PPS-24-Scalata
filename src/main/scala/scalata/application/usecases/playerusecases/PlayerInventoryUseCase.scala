package scalata.application.usecases.playerusecases

import scalata.application.usecases.PlayerUseCase
import scalata.domain.util.Direction
import scalata.domain.world.GameSession

class PlayerInventoryUseCase extends PlayerUseCase[PlayerInventoryUseCase, GameSession, String]:
  override def execute(param: String, gameSession: GameSession): GameSession = ???