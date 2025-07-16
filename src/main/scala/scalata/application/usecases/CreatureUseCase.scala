package scalata.application.usecases

import scalata.domain.world.GameSession

/** Generic action performed by a creature (player, enemy).
 *
 * <ul>
 * <li><b>U</b> – result type returned by the action.</li>
 * <li><b>P</b> – parameter consumed by the action (direction, item name, …).</li>
 * <li><code>execute</code> – pure function; takes the current
 * <code>GameSession</code> and returns an updated value of type <b>U</b>.</li>
 * </ul>
 */
trait CreatureUseCase[U, P]:
  def execute(param: P, gameSession: GameSession): U
