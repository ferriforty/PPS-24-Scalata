package scalata.application.services

import scalata.domain.entities.Player
import scalata.domain.util.{GameResult, PlayerClasses}

class PlayerFactory:
  def createPlayer(playerClass: PlayerClasses): Player =
    playerClass match
      case PlayerClasses.Mage =>
        Player(
          role = playerClass,
          health = 100,
          maxHealth = 100,
          attackPower = 10
        )
      case PlayerClasses.Barbarian =>
        Player(
          role = playerClass,
          health = 100,
          maxHealth = 100,
          attackPower = 15
        )
      case PlayerClasses.Assassin =>
        Player(
          role = playerClass,
          health = 100,
          maxHealth = 100,
          attackPower = 10
        )
