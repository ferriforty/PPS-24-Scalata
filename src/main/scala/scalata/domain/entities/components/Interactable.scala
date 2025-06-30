package scalata.domain.entities.components

import scalata.domain.entities.Entity
import scalata.domain.world.GameSession

trait Interactable:
  def interact(gameSession: GameSession): GameSession
