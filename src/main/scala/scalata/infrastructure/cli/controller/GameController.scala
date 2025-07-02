package scalata.infrastructure.cli.controller

import scalata.application.services.GameBuilder
import scalata.application.usecases.GameRunningUseCase
import scalata.domain.util.{
  Direction,
  GameControllerState,
  GameError,
  GameResult,
  PlayerCommand
}
import scalata.domain.world.GameSession
import scalata.infrastructure.cli.view.GameRunView

import scala.annotation.tailrec

class GameController(
    inputSource: () => String = () => GameRunView.getInput
) extends Controller:

  final override def start(
      worldBuilder: GameBuilder
  ): GameResult[(GameControllerState, GameBuilder)] =
    gameLoop(worldBuilder.build())

  @tailrec
  private def gameLoop(
      gameSession: GameSession
  ): GameResult[(GameControllerState, GameBuilder)] =
    GameRunView.clearScreen()
    GameRunView.displayGameState(gameSession)
    GameRunView.displayWorld(gameSession)

    GameRunningUseCase().execTurn(gameSession, processInput()) match
      case GameResult.Success(gs, _) =>
        if !gs.getGameState.note.isBlank then
          gameLoop(gs.updateGameState(gs.getGameState.withNote("")))
        else gameLoop(gs)
      case GameResult.Error(GameError.GameOver()) =>
        GameResult.success(
          GameControllerState.GameOver,
          GameBuilder(None)
        )
      case GameResult.Error(error) =>
        gameLoop(
          gameSession.updateGameState(
            gameSession.getGameState.withNote(error.message)
          )
        )

  @tailrec
  private def processInput(): Option[PlayerCommand] =

    inputSource().split("\\s+").toList match
      case direction @ ("w" | "a" | "s" | "d") :: Nil =>
        Direction
          .fromStringWASD(direction.head)
          .map(PlayerCommand.Movement.apply)
      case "a" :: direction :: Nil
          if Set("n", "s", "e", "w").contains(direction.toLowerCase) =>

        Direction.fromString(direction).map(PlayerCommand.Attack.apply)
      case "i" :: direction :: Nil
          if Set("n", "s", "e", "w").contains(direction.toLowerCase) =>

        Direction.fromString(direction).map(PlayerCommand.Interact.apply)
      case "u" :: itemName =>

        Some(PlayerCommand.Use(itemName.mkString("").toLowerCase))
      case "q" :: Nil =>

        Some(PlayerCommand.Quit)
      case _ =>
        GameRunView.displayError("Invalid Input")
        processInput()
