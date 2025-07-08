package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameResult}
import cats.effect.unsafe.implicits.global

class GameOverControllerTest extends AnyFlatSpec with Matchers:

  "GameOver" should "Return GameOver State" in:
    val controller = GameOverController()
    val resultIO = controller.start(GameBuilder(None))
    val result = resultIO.unsafeRunSync()

    result match
      case GameResult.Success(value, message) =>
        value._1 shouldBe GameControllerState.GameOver
      case GameResult.Error(error) => ()
