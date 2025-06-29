package scalata.infrastructure.cli.controller

import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class GameOverController extends Controller:
  override def start(
      worldBuilder: GameBuilder
  ): GameResult[(GameControllerState, GameBuilder)] =
    GameResult.Error(GameError.GameOver(), "Game Over")
