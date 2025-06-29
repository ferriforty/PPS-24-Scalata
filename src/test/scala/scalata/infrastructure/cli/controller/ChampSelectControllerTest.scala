package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.WorldBuilder
import scalata.domain.util.{GameControllerState, GameResult}

class ChampSelectControllerTest extends AnyFlatSpec with Matchers:

  "ChampSelect" should "Return GameRunning State" in:
    ChampSelectController(() => "m").start(worldBuilder = WorldBuilder(None)) match
      case GameResult.Success(value, _) =>
        value._1 shouldBe GameControllerState.GameRunning
      case GameResult.Error(error, message) => ()
