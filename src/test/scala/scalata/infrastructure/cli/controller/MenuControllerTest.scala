package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.domain.util.GameControllerState

class MenuControllerTest extends AnyFlatSpec with Matchers:

  "MenuController" should "Return ChampSelect State" in:
    MenuController().start() shouldBe GameControllerState.ChampSelect
