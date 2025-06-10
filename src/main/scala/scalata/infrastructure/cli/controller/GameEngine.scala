package scalata.infrastructure.cli.controller

import scalata.domain.util.GameControllerState

import scala.annotation.tailrec

class GameEngine:

  @tailrec
  final def gameLoop(engineState: GameControllerState = GameControllerState.Menu): Unit =
    engineState match
      case GameControllerState.Menu =>
        gameLoop(MenuController().start())

      case GameControllerState.ChampSelect =>
        gameLoop(ChampSelectController().start())

      case GameControllerState.GameRunning =>
        gameLoop(GameController().start())

      case GameControllerState.GameOver =>
        GameOverController().start()
