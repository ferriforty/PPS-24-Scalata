package scalata.application.usecases.playerusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.entities.components.Usable
import scalata.domain.util.{GameError, GameResult}
import scalata.domain.world.GameSession

/** Handles the **“use item”** command.
 *
 * <ul>
 * <li>Looks up the item by <code>itemName</code> in the player’s inventory.</li>
 * <li>Delegates to the <code>Usable</code> type-class to apply the effect
 * (<i>Potion</i>, <i>Weapon</i>, <i>Dust</i>, …).</li>
 * <li>Updates the player inside the <code>World</code> and returns a
 * <code>GameResult.success</code> wrapping the new
 * <code>GameSession</code>.</li>
 * <li>If the player does not own the item, returns
 * <code>GameResult.Error(GameError.ItemNotOwned)</code>.</li>
 * </ul>
 */
class PlayerInventoryUseCase
    extends CreatureUseCase[GameResult[
      GameSession
    ], String]:

  /** Apply the effect of the requested item.
   *
   * @param itemName    user-supplied identifier (case-insensitive, spaces stripped)
   * @param gameSession immutable snapshot before execution
   * @return updated session in <code>GameResult.success</code>  
   *         or <code>ItemNotOwned</code> error
   */
  override def execute(
      itemName: String,
      gameSession: GameSession
  ): GameResult[GameSession] =

    import scalata.domain.entities.items.Dust.given
    import scalata.domain.entities.items.Potion.given
    import scalata.domain.entities.items.Weapon.given

    val result =
      for
        item <- gameSession.getWorld.player.getItem(itemName)
        newPl <- Usable.tryUse(item, gameSession.getWorld.getPlayer)
        newW = gameSession.getWorld.updatePlayer(newPl)
      yield GameResult.success(gameSession.updateWorld(newW))

    result.getOrElse:
      GameResult.Error(GameError.ItemNotOwned())
