package scalata.domain.entities.components

import scalata.domain.world.GameSession

trait Interactable:
  def interact(gameSession: GameSession): GameSession
