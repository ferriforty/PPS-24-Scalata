package scalata.infrastructure.cli.controller

import scalata.domain.util.{GameControllerState, GameResult}

class GameOverController extends Controller:
  override def start(): GameResult[GameControllerState] =
    GameResult.success(GameControllerState.GameOver)
