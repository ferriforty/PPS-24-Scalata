package scalata.application.usecases.playerusecases

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.GameBuilder
import scalata.application.services.factories.{EnemyFactory, PlayerFactory}
import scalata.domain.util.EnemyClasses.Pig
import scalata.domain.util.{Direction, GameResult}
import scalata.domain.util.PlayerClasses.Mage

class PlayerAttackUseCaseTest extends AnyFlatSpec with Matchers:

  "Player" should "damage enemy" in:
    val enemy = EnemyFactory().create(Pig)
    val player = PlayerFactory().create(Mage)
    val gameSession = GameBuilder(Some(player)).build(System.currentTimeMillis())
    val direction = Direction.East

    val world = gameSession.getWorld
    val currentRoom = gameSession.getWorld
      .getRoom(gameSession.getGameState.currentRoom)
      .getOrElse(
        throw IllegalStateException("Room Not defined in Interact use case")
      )

    val newGs = gameSession.updateWorld(
      world.updateRoom(
        currentRoom.withEnemies(
          List(
            enemy.move(
              world.getPlayer.position.moveBy(direction.vector)
            )
          )
        )
      )
    )

    PlayerAttackUseCase().execute(direction, newGs) match
      case GameResult.Success(gs, _) =>
        gs.getWorld
          .getRoom(
            gs.getGameState.currentRoom
          )
          .get
          .enemies
          .exists(e =>
            e.health == enemy.health - player.attackPower
          ) shouldBe true
      case _ => ()
