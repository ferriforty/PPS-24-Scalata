package scalata.domain.entities

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.PlayerFactory
import scalata.domain.entities.components.Movable
import scalata.domain.util.{GameResult, PlayerClasses}

class PlayerTest extends AnyFlatSpec with Matchers:

  "Player" should "be movable" in :
    val player = PlayerFactory().createPlayer(PlayerClasses.Mage) match
      case GameResult.Success(p, _) => p
      case _ => ()
    player shouldBe a[Movable]

