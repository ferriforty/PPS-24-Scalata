package scalata.infrastructure.cli.controller

import cats.effect.IO
import scalata.application.services.{GameBuilder, GameView}
import scalata.application.usecases.ChampSelectUseCase
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}
import scalata.infrastructure.cli.view.{ChampSelectView, ConsoleView}

class ChampSelectController(
    view: GameView[IO]
) extends Controller:

  override def start(
      worldBuilder: GameBuilder
  ): IO[GameResult[(GameControllerState, GameBuilder)]] =

    ChampSelectUseCase().champSelect(
      processInput(ChampSelectView.champSelect(view)),
      worldBuilder
    )

  private def processInput(input: IO[String]): IO[PlayerClasses] =
    input.flatMap: raw =>
      raw.split("\\s+").toList match
        case "m" :: Nil =>
          IO.pure(PlayerClasses.Mage)
        case "b" :: Nil =>
          IO.pure(PlayerClasses.Barbarian)
        case "a" :: Nil =>
          IO.pure(PlayerClasses.Assassin)
        case _ =>
          view.displayError("Try again!") *> processInput(view.getInput)
