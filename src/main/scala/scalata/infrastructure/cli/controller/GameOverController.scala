package scalata.infrastructure.cli.controller

import scalata.application.services.WorldBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class GameOverController extends Controller:
  override def start(
      worldBuilder: WorldBuilder
  ): GameResult[(GameControllerState, WorldBuilder)] =
    GameResult.Error(GameError.GameOver(), "Game Over")
