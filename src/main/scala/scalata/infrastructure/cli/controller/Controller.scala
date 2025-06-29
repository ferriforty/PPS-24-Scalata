package scalata.infrastructure.cli.controller

import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameResult}

trait Controller:
  def start(
      worldBuilder: GameBuilder
  ): GameResult[(GameControllerState, GameBuilder)]
