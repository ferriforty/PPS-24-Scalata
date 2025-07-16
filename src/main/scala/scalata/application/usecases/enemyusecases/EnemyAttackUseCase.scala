package scalata.application.usecases.enemyusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.entities.Player
import scalata.domain.world.{GameSession, Room}

/** Executes the **enemy attack phase**.
 *
 * <h4>Algorithm</h4>
 * <ul>
 * <li>Fetch the current <code>Player</code> from the session.</li>
 * <li>For every <i>alive</i> enemy in the room:
 * <ul>
 * <li>Compute the four-tile neighbourhood around the enemy
 * (<code>neighboursFiltered</code>).</li>
 * <li>If the player’s position lies in that neighbourhood,
 * apply <code>enemy.attack(player)</code>.</li>
 * </ul>
 * </li>
 * <li>Return the player after all damage has been accumulated
 * (purely functional fold).</li>
 * </ul>
 *
 * The method is <b>pure</b>: it produces a new <code>Player</code> instance
 * without mutating the session or the room.
 */
class EnemyAttackUseCase extends CreatureUseCase[Player, Room]:

  /** Deal damage from every adjacent enemy.
   *
   * @param currentRoom room containing the enemies
   * @param gameSession immutable snapshot holding the player
   * @return updated player after taking damage
   */
  override def execute(currentRoom: Room, gameSession: GameSession): Player =
    val player = gameSession.getWorld.getPlayer

    currentRoom.getAliveEnemies
      .filter: e =>
        e.position
          .neighboursFiltered(currentRoom.isInside)
          .contains(player.position)
      .foldLeft(player): (p, e) =>
        e.attack(p)
