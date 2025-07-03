package scalata.infrastructure.cli.controller

import cats.effect.IO
import scalata.application.services.{GameBuilder, GameView}
import scalata.application.usecases.GameStartUseCase
import scalata.domain.util.{GameControllerState, GameResult}
import scalata.infrastructure.cli.view.{ConsoleView, MenuView}
import scala.annotation.tailrec

class MenuController(view: ConsoleView[IO])
    extends Controller:
  override def start(
      gameBuilder: GameBuilder
  ): IO[GameResult[(GameControllerState, GameBuilder)]] =
    
    GameStartUseCase().newGame(processInput(MenuView.menu(view)), gameBuilder)

  private def processInput(input: IO[String]): IO[Boolean] =
    input.flatMap: raw =>
      raw.trim.toLowerCase.split("\\s+").toList match
        case "y" :: Nil => IO.pure(true)
        case "n" :: Nil => IO.pure(false)
        case _ => 
          view.displayError("invalid input") *> processInput(view.getInput)
    
