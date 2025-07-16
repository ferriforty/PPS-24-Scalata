package scalata.application.usecases

import cats.effect.IO
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.GameBuilder
import scalata.application.services.factories.PlayerFactory
import scalata.domain.util.Direction.South
import scalata.domain.util.{PlayerClasses, PlayerCommand}
import org.scalatest.Tag

import scala.util.Random

class GameRunningUseCaseTest extends AnyFlatSpec with Matchers:

  it should "generate no more than 1 uncaught exception every 10k turns" taggedAs Tag("Non-Functional") in:
    var errorCount = 0
    var total = 0
    val gameSession =
      GameBuilder(
        Some(PlayerFactory().create(PlayerClasses.Mage))
      ).build(System.currentTimeMillis())

    val commandList: List[PlayerCommand] = List(
      PlayerCommand.Use(""),
      PlayerCommand.Help,
      PlayerCommand.Undo,
      PlayerCommand.Quit,
      PlayerCommand.Attack(South),
      PlayerCommand.Interact(South),
      PlayerCommand.Movement(South)
    )

    for (_ <- 1 to 10000)
      try
        GameRunningUseCase().execTurn(gameSession, IO.pure(commandList(Random.nextInt(commandList.length))))
      catch
        case _: Throwable =>
          errorCount += 1

      total += 1
    assert(errorCount <= 1, s"$errorCount exceptions in $total turns")
