package scalata.infrastructure.controller

import cats.effect.Sync
import cats.syntax.all.*
import scalata.application.services.GameBuilder
import scalata.application.usecases.GameRunningUseCase
import scalata.domain.util.{
  GameControllerState,
  GameError,
  GameResult,
  MAX_DIFFICULTY,
  PlayerCommand
}
import scalata.domain.world.GameSession
import scalata.infrastructure.view.terminal.HelpView

/** Controller that manages the **Game-Running** state.
  *
  * <ul> <li>Builds an initial [[GameSession]] from the supplied
  * [[GameBuilder]].</li> <li>Runs the main turn loop by delegating to
  * [[GameRunningUseCase]].</li> <li>Handles <code>Success</code> /
  * <code>Error</code> outcomes and decides the next
  * [[GameControllerState]].</li> </ul>
  *
  * @param askCommand
  *   side-effecting function that retrieves a [[PlayerCommand]] from the active
  *   view.
  * @tparam F
  *   effect type, usually <code>IO</code>
  */
class GameController[F[_]: Sync](
    askCommand: GameSession => F[PlayerCommand]
) extends Controller[F]:

  /** Entry-point called by the GameEngine. */
  final override def start(
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =
    gameLoop(gameBuilder.build(System.currentTimeMillis()))

  /** Recursive loop that processes one turn and recurses until the game ends or
    * a controller switch is required.
    */
  private def gameLoop(
      gameSession: GameSession
  ): F[GameResult[(GameControllerState, GameBuilder)]] =

    GameRunningUseCase()
      .execTurn(gameSession, askCommand(gameSession))
      .flatMap:
        case GameResult.Success(gs, None) if !gs.getWorld.getPlayer.isAlive =>
          GameResult
            .success(
              (
                GameControllerState.GameOver,
                GameBuilder(None)
              )
            )
            .pure[F]

        case GameResult.Success(gs, None)
            if gs.getGameState.currentLevel == gameSession.getGameState.currentLevel + 1 =>
          gameLoop(
            GameBuilder(
              player = Some(gs.getWorld.getPlayer),
              difficulty =
                (gs.getWorld.getDifficulty + 1).min(MAX_DIFFICULTY.toInt),
              level = gs.getGameState.currentLevel
            ).build(System.currentTimeMillis())
          )

        case GameResult.Success(gs, None) =>
          if gs.getGameState.note.isBlank then gameLoop(gs.store)
          else
            gameLoop(
              gs.updateGameState(
                gs.getGameState.withNote("")
              ).store
            )

        case GameResult.Success(gs, Some(note)) =>
          gameLoop(
            gs.updateGameState(
              gs.getGameState.withNote(note)
            ).store
          )

        case GameResult.Error(GameError.Help()) =>
          gameLoop(
            gameSession.updateGameState(
              gameSession.getGameState.withNote(HelpView.helpText)
            )
          )

        case GameResult.Error(GameError.GameOver()) =>
          GameResult
            .success(
              (
                GameControllerState.GameOver,
                GameBuilder(None)
              )
            )
            .pure[F]

        case GameResult.Error(GameError.Undo()) =>
          gameLoop(gameSession.undo)

        case GameResult.Error(error) =>
          gameLoop(
            gameSession.updateGameState(
              gameSession.getGameState.withNote(error.message)
            )
          )
