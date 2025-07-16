package scalata.application.services

import scalata.domain.entities.Player
import scalata.domain.world.GameSession

/** Immutable builder that assembles a complete [[GameSession]].
 *
 * <h4>Parameters</h4>
 * <ul>
 * <li><b>player</b> optional hero to start the run with; must be supplied before calling
 * [[build]].</li>
 * <li><b>difficulty</b> global difficulty level (1–10).</li>
 * <li><b>level</b> tower floor to generate (starts at <code>1</code>).</li>
 * </ul>
 *
 * All mutator methods are <b>pure</b>: they return a new `GameBuilder`
 * instance without mutating the original one.
 */
final case class GameBuilder(
    player: Option[Player],
    difficulty: Int = 1,
    level: Int = 1
):

  def withPlayer(player: Option[Player]): GameBuilder =
    copy(player = player)

  def withDifficulty(difficulty: Int): GameBuilder =
    copy(difficulty = difficulty)

  def withLevel(level: Int): GameBuilder =
    copy(level = level)

  /** Materialise the builder into a fully-fledged [[GameSession]].
   *
   * @param seed random seed forwarded to the procedural generator
   * @throws IllegalStateException if <code>player</code> is absent
   */
  def build(seed: Long): GameSession =
    FloorGenerator.generateFloor(
      player.getOrElse(
        throw new IllegalStateException("player not set in world builder")
      ),
      difficulty,
      seed,
      level
    )
