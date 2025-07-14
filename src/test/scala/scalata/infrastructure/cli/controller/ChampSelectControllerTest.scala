package scalata.infrastructure.cli.controller

import cats.effect.IO
import cats.syntax.all.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.domain.util.{GameControllerState, GameResult}
import scalata.infrastructure.cli.view.TestView
import cats.effect.unsafe.implicits.global
import scalata.application.services.GameBuilder
import scalata.infrastructure.controller.ChampSelectController
import scalata.infrastructure.view.terminal.ChampSelectView

class ChampSelectControllerTest extends AnyFlatSpec with Matchers:

  "ChampSelect" should "Return GameRunning State" in:
    val testView = new TestView("m")
    val controller =
      ChampSelectController(ChampSelectView[IO](testView).ask)
    val resultIO = controller.start(gameBuilder = GameBuilder(None))
    val result = resultIO.unsafeRunSync()

    result match
      case GameResult.Success((state, _), _) =>
        state shouldBe GameControllerState.GameRunning
      case GameResult.Error(error) =>
        fail(s"Unexpected error: $error")
