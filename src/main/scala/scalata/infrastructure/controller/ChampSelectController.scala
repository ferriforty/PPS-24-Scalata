package scalata.infrastructure.controller

import cats.effect.Sync
import scalata.application.services.GameBuilder
import scalata.application.usecases.ChampSelectUseCase
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}

class ChampSelectController[F[_] : Sync, I](
                                             askClass: F[PlayerClasses]
                                           ) extends Controller:

  override def start(
                      gameBuilder: GameBuilder
                    ): F[GameResult[(GameControllerState, GameBuilder)]] =

    ChampSelectUseCase().champSelect(
      askClass,
      gameBuilder
    )