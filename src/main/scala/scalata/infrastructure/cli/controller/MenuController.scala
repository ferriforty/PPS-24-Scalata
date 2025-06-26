package scalata.infrastructure.cli.controller

import scalata.application.services.WorldBuilder
import scalata.application.usecases.GameStartUseCase
import scalata.domain.util.{GameControllerState, GameResult}
import scalata.infrastructure.cli.view.MenuView

import scala.annotation.tailrec

class MenuController(inputSource: () => String = () => MenuView.getInput)
    extends Controller:
  override def start(worldBuilder: WorldBuilder): GameResult[(GameControllerState, WorldBuilder)] =
    MenuView.display()
    GameStartUseCase().newGame(processInput(), worldBuilder)

  @tailrec
  private def processInput(): Boolean =
    inputSource().split("\\s+").toList match
      case "y" :: Nil =>
        println("Enjoy :)")
        true
      case "n" :: Nil =>
        println("Bye Bye :( ")
        false
      case _ =>
        println("Try again!")
        processInput()
