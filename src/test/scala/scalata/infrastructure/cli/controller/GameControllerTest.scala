package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.domain.util.{GameControllerState, GameResult}

class GameControllerTest extends AnyFlatSpec with Matchers:

  "GameController" should "Return GameOver State" in:
    GameController().start() match
      case GameResult.Success(value, message) =>
        value._1 shouldBe GameControllerState.GameOver
      case GameResult.Error(error, message) => ()
