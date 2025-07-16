package scalata.application.usecases

import cats.Monad
import cats.syntax.all.*
import scalata.application.usecases.enemyusecases.{
  EnemyAttackUseCase,
  EnemyMovementUseCase
}
import scalata.application.usecases.playerusecases.{
  PlayerAttackUseCase,
  PlayerInteractUseCase,
  PlayerInventoryUseCase,
  PlayerMovementUseCase
}
import scalata.domain.util.{GameError, GameResult, PlayerCommand}
import scalata.domain.world.GameSession


/** Use-case that executes a **full game turn**:
 *
 * <h4>Flow</h4>
 * <ol>
 * <li><b>Parse player command</b> &nbsp;→&nbsp; run the matching player use-case.</li>
 * <li><b>On success</b> &nbsp;→&nbsp; apply two enemy phases in order:
 * <ul>
 * <li><code>EnemyAttackUseCase</code></li>
 * <li><code>EnemyMovementUseCase</code></li>
 * </ul>
 * </li>
 * <li><b>Error commands</b> (<i>quit / undo / help</i>) return a corresponding
 * <code>GameError</code>.</li>
 * </ol>
 *
 */
class GameRunningUseCase:

  /** Execute a single turn:
   * @param gameSession immutable snapshot before the turn starts
   * @param command     effect that yields a parsed [[PlayerCommand]]
   * @tparam F effect type with a [[cats.Monad]] instance (e.g. <code>IO</code>)
   * @return a [[scalata.domain.util.GameResult]] containing the updated
   *         [[scalata.domain.world.GameSession]] or a domain error
   */
  final def execTurn[F[_]: Monad](
      gameSession: GameSession,
      command: F[PlayerCommand]
  ): F[GameResult[GameSession]] =

    command.map: raw =>
      val turn: GameResult[GameSession] =
        raw match
          case PlayerCommand.Movement(direction) =>
            PlayerMovementUseCase().execute(direction, gameSession)
          case PlayerCommand.Attack(direction) =>
            PlayerAttackUseCase().execute(direction, gameSession)
          case PlayerCommand.Use(itemName) =>
            PlayerInventoryUseCase().execute(itemName, gameSession)
          case PlayerCommand.Interact(direction) =>
            PlayerInteractUseCase().execute(direction, gameSession)
          case PlayerCommand.Quit => GameResult.error(GameError.GameOver())
          case PlayerCommand.Undo => GameResult.error(GameError.Undo())
          case PlayerCommand.Help => GameResult.error(GameError.Help())

      turn match
        case GameResult.Success(gs, note) =>
          val currentRoom = gs.getWorld
            .getRoom(gs.getGameState.currentRoom)
            .getOrElse(
              throw IllegalStateException(
                "Room Not defined in Interact use case"
              )
            )

          GameResult.success(
            gs.updateWorld(
              gs.getWorld
                .updatePlayer(
                  EnemyAttackUseCase().execute(currentRoom, gs)
                )
                .updateRoom(
                  EnemyMovementUseCase().execute(currentRoom, gs)
                )
            ),
            note
          )
        case _ => turn
