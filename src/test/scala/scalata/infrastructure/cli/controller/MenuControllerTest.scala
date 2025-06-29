package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.WorldBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class MenuControllerTest extends AnyFlatSpec with Matchers:

  "MenuController" should "Return ChampSelect State with y" in:
    MenuController(() => "y").start(worldBuilder = WorldBuilder(None)) match
      case GameResult.Success(value, _) =>
        value._1 shouldBe GameControllerState.ChampSelect
      case GameResult.Error(_, _) => ()

  "MenuController" should "Return GameOver State with n" in:
    MenuController(() => "n").start(worldBuilder = WorldBuilder(None)) match
      case GameResult.Success(value, _) => ()
      case GameResult.Error(error, _)   =>
        error shouldBe GameError.GameOver()
