package scalata.infrastructure.controller

import cats.effect.{ExitCode, Sync}
import cats.syntax.all.*
import scalata.application.services.{GameBuilder, GamePhaseService}
import scalata.domain.util.{GameControllerState, GameResult}

class GameEngine[F[_]: Sync, I]:

  final def gameLoop(
      gamePhaseService: GamePhaseService = GamePhaseService(),
      gameBuilder: GameBuilder = GameBuilder(None),
      controllers: GameControllerState => Controller[F]
  ): F[ExitCode] =
    val controller = controllers(gamePhaseService.getCurrentPhase)

    controller
      .start(gameBuilder)
      .flatMap:
        case GameResult.Success((nextPhase, w), _) =>
          gameLoop(
            gamePhaseService.transitionTo(nextPhase),
            w,
            controllers
          )
        case GameResult.Error(_) => ExitCode.Success.pure[F]
