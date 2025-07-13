package scalata.application.usecases

import cats.Monad
import cats.syntax.all.*
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class GameStartUseCase:
  def newGame[F[_] : Monad](
                             input: F[Boolean],
                             worldBuilder: GameBuilder
                           ): F[GameResult[(GameControllerState, GameBuilder)]] =

    input.map: raw =>
      if raw then
        GameResult.success((GameControllerState.ChampSelect, worldBuilder))
      else GameResult.error(GameError.GameOver())
