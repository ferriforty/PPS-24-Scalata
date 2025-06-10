package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.domain.util.GameControllerState

class GameOverControllerTest extends AnyFlatSpec with Matchers:

  "GameOver" should "Return GameOver State" in:
    GameOverController().start() shouldBe GameControllerState.GameOver

