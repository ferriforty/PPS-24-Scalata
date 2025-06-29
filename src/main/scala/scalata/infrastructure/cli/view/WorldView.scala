package scalata.infrastructure.cli.view

import scalata.domain.util.{Direction, Point2D, WORLD_DIMENSIONS}
import scalata.domain.world.{GameSession, World}

object WorldView extends GameView:

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
            else "." // TODO check enemies and items
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
