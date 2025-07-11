package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameResult}
import scalata.infrastructure.cli.view.TestView
import cats.effect.unsafe.implicits.global

class ChampSelectControllerTest extends AnyFlatSpec with Matchers:

  "ChampSelect" should "Return GameRunning State" in:
    val testView = new TestView("m")
    val controller = ChampSelectController(testView)
    val resultIO = controller.start(gameBuilder = GameBuilder(None))
    val result = resultIO.unsafeRunSync()

    result match
      case GameResult.Success((state, _), _) =>
        state shouldBe GameControllerState.GameRunning
      case GameResult.Error(error) =>
        fail(s"Unexpected error: $error")
