package scalata.application.usecases

import cats.Monad
import cats.effect.IO
import cats.syntax.all.*
import scalata.application.services.GameBuilder
import scalata.application.services.factories.PlayerFactory
import scalata.domain.entities.Player
import scalata.domain.util.{GameControllerState, GameResult, PlayerClasses}

class ChampSelectUseCase:
  def champSelect[F[_]: Monad](
      input: F[PlayerClasses],
      worldBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =

    input.map: raw =>
      val player = PlayerFactory().create(raw)

      GameResult.success(
        (GameControllerState.GameRunning, worldBuilder.withPlayer(Some(player)))
      )
