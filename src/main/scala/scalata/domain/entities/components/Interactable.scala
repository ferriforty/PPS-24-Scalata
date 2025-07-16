package scalata.domain.entities.components

import scalata.domain.util.GameResult
import scalata.domain.world.GameSession

/** Type-class-style capability for entities that can be **interacted** with. */
trait Interactable:
  /** Perform the interaction logic.
   *
   * @param gameSession current immutable snapshot
   * @return updated snapshot or an error wrapped in <code>GameResult</code>
   */
  def interact(gameSession: GameSession): GameResult[GameSession]
