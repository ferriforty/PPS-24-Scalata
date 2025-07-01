package scalata.application.usecases.playerusecases

import scalata.application.usecases.PlayerUseCase
import scalata.domain.util.{Direction, GameResult}
import scalata.domain.world.GameSession

class PlayerInteractUseCase extends PlayerUseCase[PlayerInteractUseCase, GameResult[GameSession], Direction]:
  override def execute(param: Direction, gameSession: GameSession): GameResult[GameSession] = ???