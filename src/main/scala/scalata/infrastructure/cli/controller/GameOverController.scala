package scalata.infrastructure.cli.controller

import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult}

class GameOverController extends Controller:
  override def start(): GameResult[(GameControllerState, Option[Player])] =
    GameResult.success(GameControllerState.GameOver, None)
