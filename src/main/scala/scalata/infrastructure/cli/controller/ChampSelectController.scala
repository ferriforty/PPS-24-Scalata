package scalata.infrastructure.cli.controller

import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult}

class ChampSelectController extends Controller:
  override def start(): GameResult[(GameControllerState, Option[Player])] =
    GameResult.success(GameControllerState.GameRunning, None)
