package scalata.domain.world

import scalata.domain.entities.Player
import scalata.domain.util.Direction

/** Immutable game world.
  *
  * <p> Holds the current <b>player</b>, global <b>difficulty</b>, every
  * generated <b>room</b> and their arrangement grid. </p>
  */
final case class World(
    player: Player,
    difficulty: Int = 1,
    rooms: Map[String, Room],
    roomsArrangement: List[List[String]]
):

  /** Current difficulty level. */
  def getDifficulty: Int = difficulty

  /** The active player entity. */
  def getPlayer: Player = player

  /** Retrieve a room by id. */
  def getRoom(id: String): Option[Room] = rooms.get(id)

  /** Replace a room, returning a new world. */
  def updateRoom(room: Room): World =
    copy(rooms = rooms + (room.id -> room))

  /** Replace the player, returning a new world. */
  def updatePlayer(player: Player): World =
    copy(player = player)

  /** Get neighbouring room in the given direction, if any. */
  def getNeighbor(direction: Direction, roomId: String): Option[Room] =
    getRoom(roomId).flatMap(_.getNeighbor(direction)).flatMap(getRoom)
