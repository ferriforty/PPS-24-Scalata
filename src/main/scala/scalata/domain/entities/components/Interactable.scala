package scalata.domain.entities.components

import scalata.domain.util.GameResult
import scalata.domain.world.GameSession

trait Interactable:
  def interact(gameSession: GameSession): GameResult[GameSession]
