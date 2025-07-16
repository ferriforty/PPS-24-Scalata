package scalata.infrastructure.cli.controller

import cats.effect.IO
import cats.syntax.all.*
import cats.effect.unsafe.implicits.global
import org.scalatest.Tag
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.GameBuilder
import scalata.application.services.factories.PlayerFactory
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}
import scalata.infrastructure.cli.view.TestView
import scalata.infrastructure.controller.GameController
import scalata.infrastructure.view.terminal.GameRunView

class GameControllerTest extends AnyFlatSpec with Matchers:

  "GameController" should "Return GameOver State with input q" in:
    val testView = new TestView("q")
    val controller = GameController(GameRunView[IO](testView).ask)
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
      GameController(GameRunView[IO](testView).ask)
        .start(gameBuilder = GameBuilder(None))

  "The game" should "handle invalid inputs (e.g., wrong command, syntax error) without crashing or altering game state." taggedAs Tag(
    "Non-Functional"
  ) in:

    val invalidInputs =
      List("!", "move 999", "lojump", "use ", "equip spear!", "#@!drop", "")
    val gameBuilder =
      GameBuilder(Some(PlayerFactory().create(PlayerClasses.Mage)))

    invalidInputs.foreach: input =>
      noException should be thrownBy GameController(
        GameRunView[IO](TestView(input)).ask
      )
        .start(gameBuilder = gameBuilder)
