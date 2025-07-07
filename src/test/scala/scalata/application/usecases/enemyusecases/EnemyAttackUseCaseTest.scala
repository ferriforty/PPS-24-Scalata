package scalata.application.usecases.enemyusecases

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.GameBuilder
import scalata.application.services.factories.{EnemyFactory, PlayerFactory}
import scalata.domain.util.EnemyClasses.Pig
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.PlayerClasses.Mage


class EnemyAttackUseCaseTest extends AnyFlatSpec with Matchers:

  "Enemy" should "damage player if near" in:
    val enemy = EnemyFactory().create(Pig)
    val player = PlayerFactory().create(Mage)
    val gameSession = GameBuilder(Some(player)).build()

    val world = gameSession.getWorld
    val currentRoom = gameSession.getWorld
      .getRoom(gameSession.getGameState.currentRoom)
      .getOrElse(
        throw IllegalStateException("Room Not defined in Interact use case")
      )

    val newGs = gameSession.updateWorld(
      world.updateRoom(
        currentRoom.withEnemies(List(
          enemy.move(
            world.getPlayer.position.moveBy(Point2D(1,0))
          )
        ))
      )
    )

    EnemyAttackUseCase().execute((), newGs).health shouldBe player.health - enemy.attackPower
