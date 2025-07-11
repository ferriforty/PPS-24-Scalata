package scalata.infrastructure.cli.controller

import cats.effect.IO
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameResult}

trait Controller:
  def start(
      gameBuilder: GameBuilder
  ): IO[GameResult[(GameControllerState, GameBuilder)]]
