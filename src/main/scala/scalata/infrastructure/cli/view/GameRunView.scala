package scalata.infrastructure.cli.view

import scalata.domain.util.{Direction, Point2D, WORLD_DIMENSIONS}
import scalata.domain.world.{GameSession, Room, World}

object GameRunView extends GameView:

  def displayGameState(
                        gameSession: GameSession
                      ): Unit =
    val player = gameSession.getWorld.getPlayer

    println("\n" + ("=" * 30))
    println(s"Location: ${gameSession.getGameState.currentRoom}\n")
    println(s"HP: ${player.health}/${player.maxHealth}")
    player.weapon.foreach(w => println(s"Weapon: ${w.name} (damage: ${w.damage})"))

    if player.inventory.nonEmpty then
      println(s"Inventory: ${player.inventory.map(i => i.name).mkString(", ")}")

    gameSession.getWorld.getRoom(gameSession.getGameState.currentRoom).foreach: room =>
      if room.getAliveEnemies.nonEmpty then
        println(s"Enemies: ${room.getAliveEnemies.mkString(", ")}")

    println(
      "[W/A/S/D] Move,\n" +
        "[A]ttack with [N/S/E/W] direction of the attack,\n" +
        "[I]nteract with [N/S/E/W] direction in which to interact,\n" +
        "[U]se [name] item,\n" +
        "[Q]uit: "
    )

  def displayWorld(gameSession: GameSession): Unit =
    for y <- 0 until WORLD_DIMENSIONS._2 do
      val row = for x <- 0 until WORLD_DIMENSIONS._1 yield
        val point = Point2D(x, y)
        getCellDisplay(gameSession, point)

      println(row.mkString)

  private def getCellDisplay(gameSession: GameSession, point: Point2D): String =

    val world = gameSession.getWorld
    val state = gameSession.getGameState

    findDoorSymbol(world, point).getOrElse:

      if world.rooms.values.exists(_.isTopBotBorder(point)) then "-"
      else if world.rooms.values.exists(_.isSideBorder(point)) then "|"
      else
        world.rooms.values.find(room =>
          room.isInside(point) && state.visitedRooms.contains(room.id)
        ) match
          case Some(room) =>
            if world.player.position == point then world.player.role.toString
            else
              getEnemySymbol(room, point)
                .orElse(getItemSymbol(room, point))
                .getOrElse(".")
          case None => " "

  private def findDoorSymbol(world: World, point: Point2D): Option[String] =
    world.rooms.values
      .flatMap(room =>
        room.exits.collectFirst(_ match
          case (d, _) if room.getDoorPosition(d) == point =>
            d match
              case Direction.West  => "<"
              case Direction.East  => ">"
              case Direction.North => "^"
              case Direction.South => "v")
      )
      .headOption

  private def getEnemySymbol(room: Room, point: Point2D): Option[String] =
    room.getAliveEnemyAtPosition(point)
      .map(_.enemyType.toString)

  private def getItemSymbol(room: Room, point: Point2D): Option[String] =
    room.getItemAtPosition(point)
      .filterNot(_.isPicked)
      .map(_.toString)