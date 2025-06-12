package scalata.infrastructure.cli.controller

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class MenuControllerTest extends AnyFlatSpec with Matchers:

  "MenuController" should "Return ChampSelect State with y" in:
    MenuController(() => "y").start() match
      case GameResult.Success(value, message) => value shouldBe GameControllerState.ChampSelect
      case GameResult.Error(error, message) => ()

  "MenuController" should "Return GameOver State with n" in :
    MenuController(() => "n").start() match
      case GameResult.Success(value, message) => ()
      case GameResult.Error(error, message) => error shouldBe GameError.GameOver()
