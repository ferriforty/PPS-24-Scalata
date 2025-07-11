package scalata.application.usecases

import cats.Monad
import cats.syntax.all.*
import scalata.application.services.GameBuilder
import scalata.domain.util.{GameControllerState, GameError, GameResult}

class GameOverUseCase:
  def gameOver[F[_]: Monad](
      input: F[Boolean],
      gameBuilder: GameBuilder
  ): F[GameResult[(GameControllerState, GameBuilder)]] =

    input.map: raw =>
      if raw then GameResult.success((GameControllerState.Menu, gameBuilder))
      else GameResult.error(GameError.GameOver())
