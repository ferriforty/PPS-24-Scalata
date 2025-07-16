package scalata.infrastructure.controller

import cats.effect.{ExitCode, Sync}
import cats.syntax.all.*
import scalata.application.services.{GameBuilder, GamePhaseService}
import scalata.domain.util.{GameControllerState, GameResult}

/** Orchestrates high-level state transitions of the game.
 *
 * <ul>
 * <li>Picks the controller that matches the current
 * <code>GameControllerState</code>.</li>
 * <li>Delegates one step to that controller.</li>
 * <li>Reads the returned <code>GameResult</code> and, if successful,
 * recurses with the next phase and updated <code>GameBuilder</code>.</li>
 * <li>Ends with <code>ExitCode.Success</code> when any controller returns
 * an error (e.g. player quit).</li>
 * </ul>
 *
 * @tparam F effect type (usually <code>IO</code>)
 */
class GameEngine[F[_]: Sync]:

  /** Runs the main finite-state machine loop.
   *
   * @param gamePhaseService holder of the current phase; mutated via <code>transitionTo</code>
   * @param gameBuilder      current builder carrying player and difficulty
   * @param controllers      lookup: state → concrete controller
   */
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
