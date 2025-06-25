package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.domain.util.{GameControllerState, GameResult}

class GameOverControllerTest extends AnyFlatSpec with Matchers:

  "GameOver" should "Return GameOver State" in:
    GameOverController().start() match
      case GameResult.Success(value, message) =>
        value._1 shouldBe GameControllerState.GameOver
      case GameResult.Error(error, message) => ()
