package scalata.application.usecases

import scalata.domain.world.GameSession

trait CreatureUseCase[U, P]:
  def execute(param: P, gameSession: GameSession): U
