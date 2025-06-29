package scalata.infrastructure.cli.controller

import scalata.application.services.WorldBuilder
import scalata.application.usecases.GameRunningUseCase
import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult}

import scala.annotation.tailrec

class GameController extends Controller:

  @tailrec
  final override def start(
      worldBuilder: WorldBuilder
  ): GameResult[(GameControllerState, WorldBuilder)] =
    GameRunningUseCase().gameLoop(worldBuilder.build()) match
      case GameResult.Success((diff, player), _) =>
        start(worldBuilder.withDifficulty(diff).withPlayer(Some(player)))
      case GameResult.Error(_, _) =>
        GameResult.success(GameControllerState.GameOver, worldBuilder)
