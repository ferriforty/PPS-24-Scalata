package scalata.infrastructure.cli.controller

import cats.effect.IO
import scalata.application.services.{GameBuilder, GameView}
import scalata.application.usecases.GameOverUseCase
import scalata.domain.util.{GameControllerState, GameError, GameResult}
import scalata.infrastructure.cli.view.GameOverView

class GameOverController(
    view: GameView[IO]
) extends Controller:
  override def start(
      gameBuilder: GameBuilder
  ): IO[GameResult[(GameControllerState, GameBuilder)]] =
    GameOverUseCase().gameOver(
      processInput(GameOverView.gameOver(view)),
      gameBuilder
    )

  private def processInput(input: IO[String]): IO[Boolean] =
    input.flatMap: raw =>
      raw.split("\\s+").toList match
        case "y" :: Nil =>
          IO.pure(true)
        case "n" :: Nil =>
          IO.pure(false)
        case _ =>
          view.displayError("Try again!") *> processInput(view.getInput)
