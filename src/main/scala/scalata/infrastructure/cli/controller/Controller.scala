package scalata.infrastructure.cli.controller

import cats.effect.IO
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameResult}

trait Controller:
  def start(
      worldBuilder: GameBuilder
  ): IO[GameResult[(GameControllerState, GameBuilder)]]
