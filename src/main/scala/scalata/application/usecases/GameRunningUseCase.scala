package scalata.application.usecases

import scalata.domain.entities.Player
import scalata.domain.util.{GameResult, MAX_DIFFICULTY}
import scalata.domain.world.World

class GameRunningUseCase:

  def gameLoop(world: World): GameResult[(Int, Player)] =
    GameResult.success((world.getDifficulty + 1).min(MAX_DIFFICULTY), world.getPlayer)
