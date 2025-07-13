package scalata.infrastructure.controller

import cats.effect.Sync
import scalata.application.services.GameBuilder
import scalata.application.usecases.GameStartUseCase
import scalata.domain.util.{GameControllerState, GameResult}

class MenuController[F[_]: Sync, I](askStart: F[Boolean]) extends Controller:
  override def start(
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =

    GameStartUseCase().newGame(askStart, gameBuilder)
