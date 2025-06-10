package scalata.infrastructure.cli.controller

import scalata.domain.util.GameControllerState

class GameOverController extends Controller:
  override def start(): GameControllerState =
    GameControllerState.Menu