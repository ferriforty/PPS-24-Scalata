package scalata.domain.util

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GameErrorTest extends AnyFlatSpec with Matchers:

  "InvalidInput" should "create correct error message and code" in:
    val error = GameError.InvalidInput("s")

    error.message shouldBe "Invalid Input s"
    error.errorCode shouldBe "INVALID_INPUT"
    error shouldBe a[GameError]

