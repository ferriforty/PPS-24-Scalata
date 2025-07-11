package scalata.application.usecases.playerusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.entities.components.Usable
import scalata.domain.util.{GameError, GameResult}
import scalata.domain.world.GameSession

class PlayerInventoryUseCase
    extends CreatureUseCase[GameResult[
      GameSession
    ], String]:
  override def execute(
      itemName: String,
      gameSession: GameSession
  ): GameResult[GameSession] =

    import scalata.domain.entities.items.Potion.given
    import scalata.domain.entities.items.Dust.given
    import scalata.domain.entities.items.Weapon.given

    val result =
      for
        item <- gameSession.getWorld.player.getItem(itemName)
        newPl <- Usable.tryUse(item, gameSession.getWorld.getPlayer)
        newW = gameSession.getWorld.updatePlayer(newPl)
      yield GameResult.success(gameSession.updateWorld(newW))

    result.getOrElse:
      GameResult.Error(GameError.ItemNotOwned())
