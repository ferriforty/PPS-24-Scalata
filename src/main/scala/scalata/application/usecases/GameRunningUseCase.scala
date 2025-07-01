package scalata.application.usecases

import scalata.domain.util.{GameResult, PlayerCommand}
import scalata.domain.world.GameSession

class GameRunningUseCase:

  final def execTurn(gameSession: GameSession, command: Option[PlayerCommand]): GameResult[GameSession] =
    println(command)
    GameResult.success(gameSession)
