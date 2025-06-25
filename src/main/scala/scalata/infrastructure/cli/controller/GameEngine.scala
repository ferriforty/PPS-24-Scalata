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
    val controller = gamePhaseService.getCurrentPhase match
      case GameControllerState.Menu        => MenuController()
      case GameControllerState.ChampSelect => ChampSelectController()
      case GameControllerState.GameRunning => GameController()
      case GameControllerState.GameOver    => GameOverController()

    controller.start() match
      case GameResult.Success((nextPhase, player), _) =>
        gameLoop(
          gamePhaseService.transitionTo(nextPhase),
          Option(player).fold(worldBuilder)(worldBuilder.withPlayer)
        )
      case GameResult.Error(_, message) =>
        println(message)
