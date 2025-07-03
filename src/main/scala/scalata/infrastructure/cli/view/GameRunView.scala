package scalata.infrastructure.cli.view

import cats.effect.Sync
import cats.syntax.all.*
import scalata.application.services.GameView
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.{Direction, WORLD_DIMENSIONS}
import scalata.domain.world.{GameSession, Room, World}

object GameRunView:

  def gameRunView[F[_]: Sync](
      view: GameView[F],
      gameSession: GameSession
  ): F[String] =
    for
      _ <- view.display(
        displayGameState(gameSession = gameSession) +
          "\n\n" +
          displayWorld(gameSession = gameSession)
      )
      resp <- view.getInput
    yield resp

  private def displayGameState(
      gameSession: GameSession
  ): String =
    val player = gameSession.getWorld.getPlayer
    val note = gameSession.getGameState.note
    val room =
      gameSession.getWorld.getRoom(gameSession.getGameState.currentRoom)

    List(
      Some("\n" + "=" * 30),
      Some("Press [H] for help"),
      Option.when(note.nonEmpty)(s"Note: $note"),
      Some(s"Location: ${gameSession.getGameState.currentRoom}"),
      Some(s"HP: ${player.health}/${player.maxHealth}"),
      player.weapon.map(w => s"Weapon: ${w.name} (damage: ${w.damage})"),
      Option.when(player.inventory.nonEmpty)(
        s"Inventory: ${player.inventory.map(_.name).mkString(", ")}"
      ),
      room.flatMap(r =>
        Option.when(r.getAliveEnemies.nonEmpty)(
          s"Enemies: ${r.getAliveEnemies.mkString(", ")}"
        )
      )
    ).flatten.mkString("\n")

  private def displayWorld(gameSession: GameSession): String =
    (for (y <- 0 until WORLD_DIMENSIONS._2)
      yield (for (x <- 0 until WORLD_DIMENSIONS._1)
        yield getCellDisplay(gameSession, Point2D(x, y))).mkString)
      .mkString("\n")

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
        room.exits.collectFirst:
          case (d, _) if room.getDoorPosition(d) == point =>
            d match
              case Direction.West => "<"
              case Direction.East => ">"
              case Direction.North => "^"
              case Direction.South => "v"
      )
      .headOption

  private def getEnemySymbol(room: Room, point: Point2D): Option[String] =
    room
      .getAliveEnemyAtPosition(point)
      .map(_.enemyType.toString)

  private def getItemSymbol(room: Room, point: Point2D): Option[String] =
    room
      .getItemAtPosition(point)
      .filterNot(_.isPicked)
      .map(_.toString)
