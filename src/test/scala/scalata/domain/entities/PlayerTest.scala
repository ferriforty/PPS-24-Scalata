package scalata.domain.entities

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.factories.PlayerFactory
import scalata.domain.entities.components.{Alive, Movable}
import scalata.domain.util.{GameResult, PlayerClasses}

class PlayerTest extends AnyFlatSpec with Matchers:

  "Player" should "be movable" in:
    val player = PlayerFactory().create(PlayerClasses.Mage)
    player shouldBe a[Movable[Player]]

  "Player" should "be Alive" in:
    val player = PlayerFactory().create(PlayerClasses.Mage)
    player shouldBe a[Alive[Player]]

  "Player" should "take damage" in:
    val player = PlayerFactory().create(PlayerClasses.Mage)
    val health = player.health
    val damage = 30

    player.takeDamage(damage).health shouldBe (health - damage)

  "Player" should "heal" in:
    val player = PlayerFactory().create(PlayerClasses.Mage)
    val health = player.health
    val damage = 70
    val heal = 50

    player.heal(heal).health shouldBe player.maxHealth
    player
      .takeDamage(damage)
      .heal(heal)
      .health shouldBe (health - damage + heal)

  "Player" should "die" in:
    val player = PlayerFactory().create(PlayerClasses.Mage)

    player.isAlive shouldBe true
    player.takeDamage(player.maxHealth + 10).isAlive shouldBe false
