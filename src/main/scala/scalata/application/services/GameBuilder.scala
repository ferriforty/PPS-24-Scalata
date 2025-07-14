package scalata.application.services

import scalata.domain.entities.Player
import scalata.domain.world.GameSession

final case class GameBuilder(
    player: Option[Player],
    difficulty: Int = 1,
    level: Int = 1
):

  def withPlayer(player: Option[Player]): GameBuilder =
    copy(player = player)

  def withDifficulty(difficulty: Int): GameBuilder =
    copy(difficulty = difficulty)

  def withLevel(level: Int): GameBuilder =
    copy(level = level)

  def build(seed: Long): GameSession =
    FloorGenerator.generateFloor(
      player.getOrElse(
        throw new IllegalStateException("player not set in world builder")
      ),
      difficulty,
      seed,
      level
    )
