package scalata.application.usecases

import cats.effect.IO
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.GameBuilder
import scalata.application.services.factories.{EnemyFactory, PlayerFactory}
import scalata.domain.util.Direction.South
import scalata.domain.util.{PlayerClasses, PlayerCommand}
import org.scalatest.Tag
import scalata.domain.util.EnemyClasses.Pig
import scalata.domain.util.Geometry.Point2D

import scala.util.Random

class GameRunningUseCaseTest extends AnyFlatSpec with Matchers:

  val commandList: List[PlayerCommand] = List(
    PlayerCommand.Use(""),
    PlayerCommand.Help,
    PlayerCommand.Undo,
    PlayerCommand.Quit,
    PlayerCommand.Attack(South),
    PlayerCommand.Interact(South),
    PlayerCommand.Movement(South)
  )

  "turn cycle" should "executes in <10ms on average" taggedAs Tag(
    "Non-Functional"
  ) in:
    val start = System.nanoTime()
    val gameSession =
      GameBuilder(
        Some(PlayerFactory().create(PlayerClasses.Mage))
      ).build(System.currentTimeMillis())

    val currentRoom = gameSession.getWorld
      .getRoom(
        gameSession.getGameState.currentRoom
      )
      .get

    val enemies = List(
      EnemyFactory()
        .create(Pig)
        .move(currentRoom.botRight.moveBy(Point2D(-1, -1))),
      EnemyFactory().create(Pig).move(currentRoom.topLeft.moveBy(Point2D(1, 2)))
    )

    val gameSessionWithEnemies = gameSession.updateWorld(
      gameSession.getWorld.updateRoom(
        currentRoom.withEnemies(enemies)
      )
    )

    (1 to 10000).foreach: _ =>
      GameRunningUseCase().execTurn(
        gameSessionWithEnemies,
        IO.pure(commandList(Random.nextInt(commandList.length)))
      )

    val durationMs = (System.nanoTime() - start) / 1e6
    val avg = durationMs / 10000
    assert(avg <= 10)

  it should "generate no more than 1 uncaught exception every 10k turns" taggedAs Tag(
    "Non-Functional"
  ) in:
    var errorCount = 0
    var total = 0
    val gameSession =
      GameBuilder(
        Some(PlayerFactory().create(PlayerClasses.Mage))
      ).build(System.currentTimeMillis())

    (1 to 10000).foreach: _ =>
      try
        GameRunningUseCase().execTurn(
          gameSession,
          IO.pure(commandList(Random.nextInt(commandList.length)))
        )
      catch
        case _: Throwable =>
          errorCount += 1

      total += 1
    assert(errorCount <= 1, s"$errorCount exceptions in $total turns")
