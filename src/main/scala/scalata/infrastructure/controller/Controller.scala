package scalata.infrastructure.controller

import cats.effect.kernel.Sync
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameResult}

trait Controller[F[_]: Sync]:
  def start(
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]]
