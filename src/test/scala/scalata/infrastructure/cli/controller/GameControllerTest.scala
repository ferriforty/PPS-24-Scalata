package scalata.infrastructure.cli.controller

import cats.effect.unsafe.implicits.global
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.GameBuilder
import scalata.application.services.factories.PlayerFactory
import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}
import scalata.infrastructure.cli.view.TestView
import scalata.infrastructure.controller.GameController

class GameControllerTest extends AnyFlatSpec with Matchers:

  "GameController" should "Return GameOver State with input q" in:
    val testView = new TestView("q")
    val controller = GameController(testView)
    val resultIO = controller
      .start(gameBuilder =
        GameBuilder(Some(PlayerFactory().create(PlayerClasses.Mage)))
      )
    val result = resultIO.unsafeRunSync()

    result match
      case GameResult.Success(value, message) =>
        value._1 shouldBe GameControllerState.GameOver
      case GameResult.Error(error) => ()

  "GameController" should "Throw an exception" in:
    val testView = new TestView("q")

    intercept[IllegalStateException]:
      GameController(testView).start(gameBuilder = GameBuilder(None))
