package scalata.application.services

import scalata.domain.entities.Player
import scalata.domain.util.{GameResult, PlayerClasses}

class PlayerFactory:
  def createPlayer(playerClass: PlayerClasses): GameResult[Player] =
    playerClass match
      case PlayerClasses.Mage =>
        GameResult.success(Player(
          role = playerClass,
        ))
      case PlayerClasses.Barbarian =>
        GameResult.success(Player(
          role = playerClass,
        ))
      case PlayerClasses.Assassin =>
        GameResult.success(Player(
          role = playerClass,
        ))
