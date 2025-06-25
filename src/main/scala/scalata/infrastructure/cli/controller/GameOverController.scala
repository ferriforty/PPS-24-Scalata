package scalata.infrastructure.cli.controller

import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class GameOverController extends Controller:
  override def start(): GameResult[(GameControllerState, Option[Player])] =
    GameResult.Error(GameError.GameOver(), "Game Over")
