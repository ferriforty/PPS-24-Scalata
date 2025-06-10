package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.domain.util.GameControllerState

class GameControllerTest extends AnyFlatSpec with Matchers:

  "GameController" should "Return GameOver State" in:
    GameController().start() shouldBe GameControllerState.GameOver

