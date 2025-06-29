package scalata.infrastructure.cli.controller

import scalata.application.services.WorldBuilder
import scalata.domain.util.{GameControllerState, GameResult}

trait Controller:
  def start(
      worldBuilder: WorldBuilder
  ): GameResult[(GameControllerState, WorldBuilder)]
