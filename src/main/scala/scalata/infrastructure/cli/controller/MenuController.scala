package scalata.infrastructure.cli.controller

import scalata.domain.util.GameControllerState

class MenuController extends Controller:
  override def start(): GameControllerState =
    GameControllerState.ChampSelect