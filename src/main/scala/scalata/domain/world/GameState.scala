package scalata.domain.world

/** Immutable snapshot of the player’s progress on the current run.
 *
 * <ul>
 * <li><b>currentRoom</b> id of the room the player is standing in.</li>
 * <li><b>visitedRooms</b> set of room ids already explored on this floor.</li>
 * <li><b>currentLevel</b> tower floor index (starts at <code>1</code>).</li>
 * <li><b>note</b> short message shown to the user (help, errors, etc.).</li>
 * </ul>
 *
 * All mutators return a new <code>GameState</code>; no in-place changes.
 */
final case class GameState(
    currentRoom: String,
    visitedRooms: Set[String] = Set.empty,
    currentLevel: Int,
    note: String = ""
):

  /** Replace the note shown to the user. */
  def withNote(note: String): GameState =
    copy(note = note)

  /** Advance to the next tower level. */
  def nextLevel: GameState = copy(currentLevel = currentLevel + 1)

  /** Move the player to a new room and mark it as visited. */
  def setRoom(newRoom: String): GameState =
    copy(currentRoom = newRoom, visitedRooms = visitedRooms + newRoom)
