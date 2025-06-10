package scalata.infrastructure.cli.controller

import scalata.domain.util.GameControllerState

trait Controller:
  def start(): GameControllerState
  
