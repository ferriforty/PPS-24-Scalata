package scalata.application.services

import scalata.domain.entities.Player
import scalata.domain.util.PlayerClasses

class PlayerFactory:
  def createPlayer(playerClass: PlayerClasses): Player =
    playerClass match
      case PlayerClasses.Mage => Player()
      case PlayerClasses.Barbarian => Player()
      case PlayerClasses.Assassin => Player()


