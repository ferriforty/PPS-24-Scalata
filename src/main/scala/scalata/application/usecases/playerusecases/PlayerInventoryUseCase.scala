package scalata.application.usecases.playerusecases

import scalata.application.usecases.PlayerUseCase
import scalata.domain.entities.components.Usable
import scalata.domain.util.{GameError, GameResult}
import scalata.domain.world.GameSession

class PlayerInventoryUseCase extends PlayerUseCase[PlayerInventoryUseCase, GameResult[GameSession], String]:
  override def execute(param: String, gameSession: GameSession): GameResult[GameSession] =

    import scalata.domain.entities.items.Potion.given
    import scalata.domain.entities.items.Dust.given
    import scalata.domain.entities.items.Weapon.given

    val result =
      for
        item <- gameSession.getWorld.player.getItem(param)
        newPl <- Usable.tryUse(item, gameSession.getWorld.getPlayer)
        newW = gameSession.getWorld.updatePlayer(newPl)
      yield GameResult.success(gameSession.updateWorld(newW))

    println(gameSession.getWorld.player.getItem(param).get.name)
    println(result)
    result.getOrElse:
      GameResult.Error(GameError.ItemNotOwned())