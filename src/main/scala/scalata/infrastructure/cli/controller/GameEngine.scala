package scalata.infrastructure.cli.controller

import scalata.application.services.{GamePhaseService, GameBuilder}
import scalata.domain.util.{GameControllerState, GameResult}

import scala.annotation.tailrec

class GameEngine:

  @tailrec
  final def gameLoop(
      gamePhaseService: GamePhaseService = GamePhaseService(),
      worldBuilder: GameBuilder = GameBuilder(None)
  ): Unit =
    val controller = gamePhaseService.getCurrentPhase match
      case GameControllerState.Menu        => MenuController()
      case GameControllerState.ChampSelect => ChampSelectController()
      case GameControllerState.GameRunning => GameController()
      case GameControllerState.GameOver    => GameOverController()

    controller.start(worldBuilder) match
      case GameResult.Success((nextPhase, w), _) =>
        gameLoop(
          gamePhaseService.transitionTo(nextPhase),
          w
        )
      case GameResult.Error(_, message) =>
        println(message)
