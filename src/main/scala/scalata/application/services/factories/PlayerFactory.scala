package scalata.application.services.factories

import scalata.application.services.EntityFactory
import scalata.domain.entities.Player
import scalata.domain.util.{GameResult, PlayerClasses}

class PlayerFactory extends EntityFactory[PlayerFactory, Player, PlayerClasses]:
  def create(playerClass: PlayerClasses, id: String = "p1"): Player =
    playerClass match
      case PlayerClasses.Mage =>
        Player(
          id = id,
          role = playerClass,
          health = 100,
          maxHealth = 100,
          attackPower = 10
        )
      case PlayerClasses.Barbarian =>
        Player(
          id = id,
          role = playerClass,
          health = 100,
          maxHealth = 100,
          attackPower = 15
        )
      case PlayerClasses.Assassin =>
        Player(
          id = id,
          role = playerClass,
          health = 100,
          maxHealth = 100,
          attackPower = 10
        )
