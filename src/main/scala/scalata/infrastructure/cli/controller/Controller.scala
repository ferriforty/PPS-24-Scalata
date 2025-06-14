package scalata.infrastructure.cli.controller

import scalata.domain.util.{GameControllerState, GameResult}

trait Controller:
  def start(): GameResult[GameControllerState]
