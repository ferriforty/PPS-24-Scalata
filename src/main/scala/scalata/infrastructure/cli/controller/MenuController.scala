package scalata.infrastructure.cli.controller

import scalata.application.usecases.GameStartUseCase
import scalata.domain.util.{GameControllerState, GameResult}
import scalata.infrastructure.cli.view.MenuView

import scala.annotation.tailrec

class MenuController(inputSource: () => String = () => MenuView.getInput) extends Controller:
  override def start(): GameResult[GameControllerState] =
    MenuView.display()
    GameStartUseCase().newGame(processMenuInput())

  @tailrec
  private def processMenuInput(): Boolean =
    inputSource().split("\\s+").toList match
      case "y" :: Nil =>
        println("Enjoy :)")
        true
      case "n" :: Nil =>
        println("Why did you open ittt!! \n  Bye Bye :( ")
        false
      case _ =>
        println("Try again you silly!")
        processMenuInput()