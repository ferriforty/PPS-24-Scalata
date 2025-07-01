package scalata.application.usecases.playerusecases

import scalata.application.usecases.PlayerUseCase
import scalata.domain.util.Direction
import scalata.domain.world.GameSession

class PlayerMovementUseCase extends PlayerUseCase[PlayerMovementUseCase, GameSession, Direction]:
  override def execute(param: Direction, gameSession: GameSession): GameSession = ???
