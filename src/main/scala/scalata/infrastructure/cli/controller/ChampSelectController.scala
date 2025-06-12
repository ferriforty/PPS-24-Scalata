package scalata.infrastructure.cli.controller

import scalata.domain.util.{GameControllerState, GameResult}

class ChampSelectController extends Controller:
  override def start(): GameResult[GameControllerState] =
    GameResult.success(GameControllerState.GameRunning)