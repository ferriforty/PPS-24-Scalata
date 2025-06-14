package scalata.application.services

import scalata.domain.entities.Player
import scalata.domain.world.{FloorGenerator, World}

final case class WorldBuilder(
    player: Option[Player]
):

  def withPlayer(player: Option[Player]): WorldBuilder =
    copy(player)

  def build(difficulty: Int): World =
    FloorGenerator.generateFloor(
      player.getOrElse(
        throw new IllegalStateException("player not set in world builder")
      ),
      difficulty,
      System.currentTimeMillis()
    )
