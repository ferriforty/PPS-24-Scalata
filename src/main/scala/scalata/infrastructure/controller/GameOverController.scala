package scalata.infrastructure.controller

import cats.effect.Sync
import scalata.application.services.GameBuilder
import scalata.application.usecases.GameOverUseCase
import scalata.domain.util.{GameControllerState, GameResult}

class GameOverController[F[_] : Sync, I](
                                          askRestart: F[Boolean]
                                        ) extends Controller:
  override def start(
                      gameBuilder: GameBuilder
                    ): F[GameResult[(GameControllerState, GameBuilder)]] =
    GameOverUseCase().gameOver(
      askRestart,
      gameBuilder
    )
