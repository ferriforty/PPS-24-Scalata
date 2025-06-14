package scalata.application.services

import scalata.domain.util.{GameControllerState, GameResult}

case class GamePhaseService(
    currentPhase: GameControllerState = GameControllerState.Menu
):
  def getCurrentPhase: GameControllerState = currentPhase

  def transitionTo(newPhase: GameControllerState): GamePhaseService =
    copy(currentPhase = newPhase)
