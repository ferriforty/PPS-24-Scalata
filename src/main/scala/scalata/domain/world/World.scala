package scalata.domain.world

import scalata.domain.entities.Player
import scalata.domain.util.Direction

final case class World(
                      player: Player,
                      difficulty: Int = 1,
                      rooms: Map[Int, Room]
                      ):

  def getRoom(id: Int): Option[Room] = rooms.get(id)

  def updateRoom(room: Room): World =
    copy(rooms = rooms + (room.id -> room))

  def updatePlayer(player: Player): World =
    copy(player = player)

  def getNeighbor(direction: Direction, roomId: Int): Option[Room] =
    getRoom(roomId).flatMap(_.getNeighbor(direction)).flatMap(getRoom)
