package scalata.application.usecases.playerusecases

import scalata.application.usecases.PlayerUseCase
import scalata.domain.util.{Direction, GameResult}
import scalata.domain.world.GameSession

class PlayerInventoryUseCase extends PlayerUseCase[PlayerInventoryUseCase, GameResult[GameSession], String]:
  override def execute(param: String, gameSession: GameSession): GameResult[GameSession] = ???