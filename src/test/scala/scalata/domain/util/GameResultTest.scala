package scalata.domain.util

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameResultTest extends AnyFlatSpec with Matchers:

  "GameResult.success" should "create a Success instance with value" in:
    val result = GameResult.success(42)

    result shouldBe a[GameResult.Success[_]]
    result match
      case GameResult.Success(value, message) =>
        message shouldBe None
        value shouldBe 42
      case _ => fail("Expected Success")

  it should "create a Success instance with value and message" in:
    val result = GameResult.Success(100, Some("Level completed"))

    result match
      case GameResult.Success(value, message) =>
        value shouldBe 100
        message shouldBe Some("Level completed")
      case _ => fail("Expected Success")

  "GameResult.error" should "create an Error instance" in:
    val gameError = GameError.InvalidInput("s")
    val result = GameResult.error(gameError, "invalid input")

    result shouldBe a[GameResult.Error]
    result match
      case GameResult.Error(error, message) =>
        error shouldBe gameError
        message shouldBe "invalid input"
      case _ => fail("Expected Error")
