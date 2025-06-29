package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.WorldBuilder
import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}

class GameControllerTest extends AnyFlatSpec with Matchers:

  "GameController" should "Return GameOver State" in:
    GameController().start(worldBuilder =
      WorldBuilder(Some(Player(role = PlayerClasses.Mage)))
    ) match
      case GameResult.Success(value, message) =>
        value._1 shouldBe GameControllerState.GameOver
      case GameResult.Error(error, message) => ()

  "GameController" should "Throw an exception" in:
    intercept[IllegalStateException]:
      GameController().start(worldBuilder = WorldBuilder(None))
