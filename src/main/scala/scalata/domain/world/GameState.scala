package scalata.domain.world

final case class GameState(
    currentRoom: String,
    visitedRooms: Set[String] = Set.empty,
    currentLevel: Int
):

  def setRoom(newRoom: String): GameState =
    copy(currentRoom = newRoom, visitedRooms = visitedRooms + newRoom)
