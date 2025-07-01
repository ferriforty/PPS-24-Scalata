package scalata.infrastructure.cli.controller

import scalata.application.services.GameBuilder
import scalata.application.usecases.ChampSelectUseCase
import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}
import scalata.infrastructure.cli.view.ChampSelectView

import scala.annotation.tailrec

class ChampSelectController(
    inputSource: () => String = () => ChampSelectView.getInput
) extends Controller:

  override def start(
      worldBuilder: GameBuilder
  ): GameResult[(GameControllerState, GameBuilder)] =
    ChampSelectView.display()
    ChampSelectUseCase().champSelect(processInput(), worldBuilder)

  @tailrec
  private def processInput(): PlayerClasses =
    inputSource().split("\\s+").toList match
      case "m" :: Nil =>
        ChampSelectView.display("Abracadabra.")
        PlayerClasses.Mage
      case "b" :: Nil =>
        ChampSelectView.display("War is the business of barbarians.")
        PlayerClasses.Barbarian
      case "a" :: Nil =>
        ChampSelectView.display("Nothing is true, everything is permitted.")
        PlayerClasses.Assassin
      case _ =>
        ChampSelectView.display("Try again!")
        processInput()
