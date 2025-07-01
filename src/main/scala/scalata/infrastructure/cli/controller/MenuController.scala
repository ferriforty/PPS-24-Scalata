package scalata.infrastructure.cli.controller

import scalata.application.services.GameBuilder
import scalata.application.usecases.GameStartUseCase
import scalata.domain.util.{GameControllerState, GameResult}
import scalata.infrastructure.cli.view.MenuView

import scala.annotation.tailrec

class MenuController(inputSource: () => String = () => MenuView.getInput)
    extends Controller:
  override def start(
      gameBuilder: GameBuilder
  ): GameResult[(GameControllerState, GameBuilder)] =
    MenuView.display()
    GameStartUseCase().newGame(processInput(), gameBuilder)

  @tailrec
  private def processInput(): Boolean =
    inputSource().split("\\s+").toList match
      case "y" :: Nil =>
        MenuView.display("Enjoy :)")
        true
      case "n" :: Nil =>
        MenuView.display("Bye Bye :( ")
        false
      case _ =>
        MenuView.display("Try again!")
        processInput()
