package scalata.infrastructure.cli.controller

import scalata.domain.util.{GameControllerState, GameError, GameResult}
import scalata.infrastructure.cli.view.MenuView

import scala.annotation.tailrec

class MenuController(inputSource: () => String = () => MenuView.getInput) extends Controller:
  override def start(): GameResult[GameControllerState] =
    MenuView.display()
    if processMenuInput(inputSource) then
      GameResult.success(GameControllerState.ChampSelect)
    else
      GameResult.error(GameError.GameOver(), "game over")

  @tailrec
  private def processMenuInput(input: () => String): Boolean =
    input().split("\\s+").toList match
      case "y" :: Nil =>
        println("Enjoy :)")
        true
      case "n" :: Nil =>
        println("Why did you open ittt!! \n  Bye Bye :( ")
        false
      case _ =>
        println("Try again you silly!")
        processMenuInput(input)