package scalata.infrastructure.cli.controller

import scalata.application.services.{GamePhaseService, WorldBuilder}
import scalata.domain.util.{GameControllerState, GameResult}

import scala.annotation.tailrec

class GameEngine:

  @tailrec
  final def gameLoop(
      gamePhaseService: GamePhaseService = GamePhaseService(),
      worldBuilder: WorldBuilder = WorldBuilder(None)
  ): Unit =
    gamePhaseService.getCurrentPhase match
      case GameControllerState.Menu =>
        MenuController().start() match
          case GameResult.Success(x, _) =>
            gameLoop(gamePhaseService.transitionTo(x._1), worldBuilder)
          case GameResult.Error(_, message) => println(message)

      case GameControllerState.ChampSelect =>
        ChampSelectController().start() match
          case GameResult.Success(x, _) =>
            gameLoop(
              gamePhaseService.transitionTo(x._1),
              worldBuilder.withPlayer(x._2)
            )
          case GameResult.Error(_, message) => println(message)

      case GameControllerState.GameRunning =>
        GameController().start() match
          case GameResult.Success(x, _) =>
            gameLoop(gamePhaseService.transitionTo(x._1), worldBuilder)
          case GameResult.Error(_, message) => println(message)

      case GameControllerState.GameOver =>
        GameOverController().start() match
          case GameResult.Success(x, _) =>
            gameLoop(gamePhaseService.transitionTo(x._1), worldBuilder)
          case GameResult.Error(_, message) => println(message)
