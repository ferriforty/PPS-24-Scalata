package scalata.application.usecases

import scalata.domain.world.GameSession

trait PlayerUseCase[F <: PlayerUseCase[F, U, P], U, P]:
  def execute(param: P, gameSession: GameSession): U
