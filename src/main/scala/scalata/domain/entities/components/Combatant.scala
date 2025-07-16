package scalata.domain.entities.components

/** Entity that can engage in combat.
 *
 * <ul>
 * <li><b>attackPower</b> – base damage dealt by the entity.</li>
 * <li><b>attack</b> – pure function: returns a new instance of the opponent
 * after damage has been applied.</li>
 * </ul>
 *
 * @tparam E concrete opponent type
 */
trait Combatant[E]:
  val attackPower: Int

  /** Resolve an attack against <code>opponent</code>. */
  def attack(opponent: E): E
