package scalata.infrastructure.cli.controller

import scalata.application.services.GameBuilder
import scalata.application.usecases.GameRunningUseCase
import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult}
import scalata.domain.world.GameSession

import scala.annotation.tailrec

class GameController extends Controller:

  @tailrec
  final override def start(
      worldBuilder: GameBuilder
  ): GameResult[(GameControllerState, GameBuilder)] =
    GameRunningUseCase().gameLoop(worldBuilder.build()) match
      case GameResult.Success((diff, player, level), _) =>
        start(
          worldBuilder
            .withDifficulty(diff)
            .withPlayer(Some(player))
            .withLevel(level)
        )
      case GameResult.Error(_, _) =>
        GameResult.success(GameControllerState.GameOver, worldBuilder)
