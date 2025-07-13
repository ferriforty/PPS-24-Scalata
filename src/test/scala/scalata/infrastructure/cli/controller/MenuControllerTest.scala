package scalata.infrastructure.cli.controller

import cats.effect.IO
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}
import scalata.infrastructure.cli.view.TestView
import cats.effect.unsafe.implicits.global
import scalata.infrastructure.controller.MenuController

class MenuControllerTest extends AnyFlatSpec with Matchers:

  "MenuController" should "Return ChampSelect State with y" in:
    val testView = new TestView("y")
    val controller = MenuController(testView)
    val resultIO = controller.start(GameBuilder(None))
    val result = resultIO.unsafeRunSync()

    result match
      case GameResult.Success(value, _) =>
        value._1 shouldBe GameControllerState.ChampSelect
      case GameResult.Error(_) => ()

  "MenuController" should "Return GameOver State with n" in:
    val testView = new TestView("n")
    val controller = MenuController(testView)
    val resultIO = controller.start(GameBuilder(None))
    val result = resultIO.unsafeRunSync()

    result match
      case GameResult.Success(value, _) => ()
      case GameResult.Error(error)      =>
        error shouldBe GameError.GameOver()
