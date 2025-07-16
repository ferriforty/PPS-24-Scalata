package scalata.application.usecases.playerusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.{Direction, GameResult}
import scalata.domain.world.GameSession

/** Executes a **player attack** in the chosen direction.
  *
  * <h4>Algorithm</h4> <ul> <li>Compute a *reach vector* whose length equals the
  * player’s <code>reach</code> stat.</li> <li>Build the inclusive line of
  * points from the player’s position to the target tile
  * (<code>rangeTo</code>).</li> <li>For every enemy in the current room: <ul>
  * <li>If the enemy is <i>alive</i> **and** its position lies on the line,
  * apply <code>player.attack</code>.</li> </ul> </li> <li>Return an updated
  * <code>GameSession</code> wrapped in <code>GameResult.success</code>.</li>
  * </ul>
  *
  * <b>Failure modes</b> If the room referenced by
  * <code>GameState.currentRoom</code> is missing, an
  * <code>IllegalStateException</code> is thrown (world corruption guard).
  */
class PlayerAttackUseCase
    extends CreatureUseCase[GameResult[
      GameSession
    ], Direction]:

  /** Perform one attack action.
    *
    * @param direction
    *   direction chosen by the player
    * @param gameSession
    *   immutable snapshot before the attack
    * @return
    *   updated snapshot wrapped in a successful <code>GameResult</code>
    */
  override def execute(
      direction: Direction,
      gameSession: GameSession
  ): GameResult[GameSession] =
    val player = gameSession.getWorld.player
    val currentRoom = gameSession.getWorld
      .getRoom(gameSession.getGameState.currentRoom)
      .getOrElse(
        throw new IllegalStateException(
          "Room Not defined in attack use case"
        )
      )
    val reachVector = direction match
      case Direction.North => Point2D(0, -player.reach())
      case Direction.South => Point2D(0, player.reach())
      case Direction.West  => Point2D(-player.reach(), 0)
      case Direction.East  => Point2D(player.reach(), 0)

    val attackLine = player.position.rangeTo(
      player.position.moveBy(reachVector)
    )

    GameResult.success(
      gameSession.updateWorld(
        gameSession.getWorld.updateRoom(
          currentRoom.withEnemies(
            currentRoom.enemies.map: e =>
              if e.isAlive && attackLine.contains(e.position) then
                player.attack(e)
              else e
          )
        )
      )
    )
