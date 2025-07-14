package scalata.domain.world

final case class GameState(
    currentRoom: String,
    visitedRooms: Set[String] = Set.empty,
    currentLevel: Int,
    note: String = ""
):

  def withNote(note: String): GameState =
    copy(note = note)
    
  def nextLevel: GameState = copy(currentLevel = currentLevel + 1)

  def setRoom(newRoom: String): GameState =
    copy(currentRoom = newRoom, visitedRooms = visitedRooms + newRoom)
