package scalata.infrastructure.cli.controller

import cats.effect.IO
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class GameOverController extends Controller:
  override def start(
      worldBuilder: GameBuilder
  ): IO[GameResult[(GameControllerState, GameBuilder)]] =
    IO.pure(GameResult.Error(GameError.GameOver()))
