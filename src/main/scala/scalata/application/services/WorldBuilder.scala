package scalata.application.services

import scalata.domain.entities.Player
import scalata.domain.world.{FloorGenerator, World}

final case class WorldBuilder(
    player: Option[Player],
    difficulty: Int = 1
):

  def withPlayer(player: Option[Player]): WorldBuilder =
    copy(player = player)

  def withDifficulty(difficulty: Int): WorldBuilder =
    copy(difficulty = difficulty)

  def build(): World =
    FloorGenerator.generateFloor(
      player.getOrElse(
        throw new IllegalStateException("player not set in world builder")
      ),
      difficulty,
      System.currentTimeMillis()
    )
