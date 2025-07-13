package scalata.domain.world

import scalata.domain.entities.Player
import scalata.domain.util.Direction

final case class World(
    player: Player,
    difficulty: Int = 1,
    rooms: Map[String, Room],
    roomsArrangement: List[List[String]]
):

  def getDifficulty: Int = difficulty

  def getPlayer: Player = player

  def getRoom(id: String): Option[Room] = rooms.get(id)

  def updateRoom(room: Room): World =
    copy(rooms = rooms + (room.id -> room))

  def updatePlayer(player: Player): World =
    copy(player = player)

  def getNeighbor(direction: Direction, roomId: String): Option[Room] =
    getRoom(roomId).flatMap(_.getNeighbor(direction)).flatMap(getRoom)
