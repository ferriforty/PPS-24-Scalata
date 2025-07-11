package scalata.infrastructure.cli.controller

import cats.effect.{ExitCode, IO}
import scalata.application.services.{GameBuilder, GamePhaseService, GameView}
import scalata.domain.util.{GameControllerState, GameResult}
import scalata.infrastructure.cli.view.ConsoleView

import scala.annotation.tailrec

class GameEngine:

  final def gameLoop(
      gamePhaseService: GamePhaseService = GamePhaseService(),
      worldBuilder: GameBuilder = GameBuilder(None),
      view: GameView[IO]
  ): IO[ExitCode] =
    val controller = gamePhaseService.getCurrentPhase match
      case GameControllerState.Menu        => MenuController(view)
      case GameControllerState.ChampSelect => ChampSelectController(view)
      case GameControllerState.GameRunning => GameController(view)
      case GameControllerState.GameOver    => GameOverController(view)

    controller
      .start(worldBuilder)
      .flatMap:
        case GameResult.Success((nextPhase, w), _) =>
          gameLoop(
            gamePhaseService.transitionTo(nextPhase),
            w,
            view
          )
        case GameResult.Error(_) => IO.pure(ExitCode.Success)
