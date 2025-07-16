package scalata.application.services

import scalata.domain.util.GameControllerState

/** Mutable-looking, yet **immutable**, holder of the current
  * finite-state-machine phase.
  *
  * <h4>Responsibilities</h4> <ul> <li>Expose the <b>current phase</b> through
  * <code>getCurrentPhase</code>.</li> <li>Create a <em>new</em> instance with
  * an updated phase via <code>transitionTo</code> (pure copy, no
  * mutation).</li> </ul>
  *
  * This minimal wrapper exists so that the <code>GameEngine</code> can pass a
  * single value around and still treat the phase as a first-class object.
  *
  * @param currentPhase
  *   FSM node the game is presently in; defaults to
  *   <code>GameControllerState.Menu</code>.
  */
case class GamePhaseService(
    currentPhase: GameControllerState = GameControllerState.Menu
):
  def getCurrentPhase: GameControllerState = currentPhase

  /** Produce a new service pointing to <code>newPhase</code>. */
  def transitionTo(newPhase: GameControllerState): GamePhaseService =
    copy(currentPhase = newPhase)
