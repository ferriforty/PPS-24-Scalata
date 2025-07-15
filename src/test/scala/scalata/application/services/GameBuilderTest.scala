package scalata.application.services

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.factories.PlayerFactory
import scalata.domain.util.PlayerClasses.{Barbarian, Mage}

class GameBuilderTest extends AnyFlatSpec with Matchers:

  "GameBuilder" should "throw IllegalStateException if player is not set" in:
    val builder = GameBuilder(None)
    an[IllegalStateException] should be thrownBy:
      builder.build(123L)

  it should "build a GameSession with the given player, difficulty, level, and seed" in:
    val player = PlayerFactory().create(Mage)
    val level = 5
    val difficulty = 3

    val builder = GameBuilder(Some(player))
      .withDifficulty(difficulty)
      .withLevel(level)

    val gameSession = builder.build(123L)

    gameSession should not be null
    gameSession.getWorld.getPlayer.toString shouldBe player.toString
    gameSession.getGameState.currentLevel shouldBe level
    gameSession.getWorld.difficulty shouldBe difficulty

  it should "allow updating player, difficulty, and level" in:
    val level2 = 6
    val diff2 = 5
    val player1 = PlayerFactory().create(Mage)
    val player2 = PlayerFactory().create(Barbarian)
    val builder = GameBuilder(Some(player1))
      .withDifficulty(2)
      .withLevel(4)

    val updatedBuilder =
      builder.withPlayer(Some(player2)).withDifficulty(diff2).withLevel(level2)

    updatedBuilder.player shouldEqual Some(player2)
    updatedBuilder.difficulty shouldEqual diff2
    updatedBuilder.level shouldEqual level2
