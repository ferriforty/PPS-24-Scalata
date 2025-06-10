package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.domain.util.GameControllerState

class ChampSelectControllerTest extends AnyFlatSpec with Matchers:

  "ChampSelect" should "Return GameRunning State" in:
    ChampSelectController().start() shouldBe GameControllerState.GameRunning

