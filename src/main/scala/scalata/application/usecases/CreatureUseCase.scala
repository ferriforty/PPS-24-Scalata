package scalata.application.usecases

import scalata.domain.world.GameSession

trait CreatureUseCase[F <: CreatureUseCase[F, U, P], U, P]:
  def execute(param: P, gameSession: GameSession): U
