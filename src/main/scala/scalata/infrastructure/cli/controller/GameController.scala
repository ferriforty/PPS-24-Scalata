package scalata.infrastructure.cli.controller

import cats.effect.IO
import scalata.application.services.{GameBuilder, GameView}
import scalata.application.usecases.GameRunningUseCase
import scalata.domain.world.World
import scalata.domain.entities.Player
import scalata.domain.util.{
  Direction,
  GameControllerState,
  GameError,
  GameResult,
  PlayerCommand
}
import scalata.domain.world.GameSession
import scalata.infrastructure.cli.view.{ConsoleView, GameRunView}

class GameController(
    view: GameView[IO]
) extends Controller:

  final override def start(
      gameBuilder: GameBuilder
  ): IO[GameResult[(GameControllerState, GameBuilder)]] =
    gameLoop(gameBuilder.build())

  private def gameLoop(
      gameSession: GameSession
  ): IO[GameResult[(GameControllerState, GameBuilder)]] =
    val input = GameRunView.gameRunView(view, gameSession)

    GameRunningUseCase()
      .execTurn(gameSession, processInput(input))
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
          IO.pure(
            GameResult.success(
              (
                GameControllerState.GameOver,
                GameBuilder(None)
              )
            )
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

        case GameResult.Error(GameError.GameOver()) =>
          IO.pure(
            GameResult.success(
              (
                GameControllerState.GameOver,
                GameBuilder(None)
              )
            )
          )

        case GameResult.Error(GameError.Undo()) =>
          gameLoop(gameSession.undo)

        case GameResult.Error(error) =>
          gameLoop(
            gameSession.updateGameState(
              gameSession.getGameState.withNote(error.message)
            )
          )

  private def processInput(input: IO[String]): IO[Option[PlayerCommand]] =

    input.flatMap: raw =>
      raw.split("\\s+").toList match
        case direction @ ("w" | "a" | "s" | "d") :: Nil =>

          IO.pure(
            Direction
              .fromStringWASD(direction.head)
              .map(PlayerCommand.Movement.apply)
          )
        case "a" :: direction :: Nil
            if Set("n", "s", "e", "w").contains(direction.toLowerCase) =>

          IO.pure(
            Direction.fromString(direction).map(PlayerCommand.Attack.apply)
          )
        case "i" :: direction :: Nil
            if Set("n", "s", "e", "w").contains(direction.toLowerCase) =>

          IO.pure(
            Direction.fromString(direction).map(PlayerCommand.Interact.apply)
          )
        case "u" :: itemName =>

          IO.pure(Some(PlayerCommand.Use(itemName.mkString("").toLowerCase)))
        case "h" :: itemName =>

          IO.pure(Some(PlayerCommand.Help))
        case "q" :: Nil =>

          IO.pure(Some(PlayerCommand.Quit))
        case "undo" :: Nil =>

          IO.pure(Some(PlayerCommand.Undo))
        case _ =>
          view.displayError("Invalid Input") *> processInput(view.getInput)
