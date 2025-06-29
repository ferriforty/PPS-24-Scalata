package scalata.application.usecases

import scalata.domain.entities.Player
import scalata.domain.util.{GameError, GameResult, MAX_DIFFICULTY}
import scalata.domain.world.GameSession
import scalata.infrastructure.cli.view.WorldView

class GameRunningUseCase:

  def gameLoop(gameSession: GameSession): GameResult[(Int, Player, Int)] =
    WorldView.displayWorld(gameSession)
    GameResult.error(GameError.GameOver(), "")
    // GameResult.success((world.getDifficulty + 1).min(MAX_DIFFICULTY), world.getPlayer)
