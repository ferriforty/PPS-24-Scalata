package scalata.infrastructure.controller

import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameResult}

trait Controller[F[_]]:
  def start(
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]]
