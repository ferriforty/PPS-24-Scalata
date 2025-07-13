package scalata.infrastructure.controller

import cats.effect.Sync
import cats.syntax.all.*
import scalata.application.services.GameBuilder
import scalata.application.usecases.GameRunningUseCase
import scalata.domain.entities.Player
import scalata.domain.util.{
  GameControllerState,
  GameError,
  GameResult,
  PlayerCommand
}
import scalata.domain.world.{GameSession, World}
import scalata.infrastructure.view.terminal.HelpView

class GameController[F[_]: Sync](
    askCommand: GameSession => F[PlayerCommand]
) extends Controller:

  final override def start(
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =
    gameLoop(gameBuilder.build())

  private def gameLoop(
      gameSession: GameSession
  ): F[GameResult[(GameControllerState, GameBuilder)]] =

    GameRunningUseCase()
      .execTurn(gameSession, askCommand(gameSession))
      .flatMap:
        case GameResult.Success(
              GameSession(
                World(
                  Player(_, _, _, 0, _, _, _, _, _),
                  _,
                  _,
                  _
                ),
                _,
                _
              ),
              None
            ) =>
          GameResult
            .success(
              (
                GameControllerState.GameOver,
                GameBuilder(None)
              )
            )
            .pure[F]

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
