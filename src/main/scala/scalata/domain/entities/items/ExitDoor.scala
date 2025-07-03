package scalata.domain.entities.items

import scalata.application.services.GameBuilder
import scalata.domain.entities.Item
import scalata.domain.util.{GameResult, ItemClasses, MAX_DIFFICULTY}
import scalata.domain.util.Geometry.Point2D
import scalata.domain.world.GameSession

final case class ExitDoor(
    override val position: Option[Point2D] = None,
    override val name: String = "Exit Door",
    override val itemClass: ItemClasses = ItemClasses.ExitDoor
) extends Item:

  private def setPosition(pos: Option[Point2D]): ExitDoor = copy(position = pos)

  override def interact(gameSession: GameSession): GameResult[GameSession] =
    GameResult.success(GameBuilder(
      player = Some(gameSession.getWorld.getPlayer),
      difficulty = (gameSession.getWorld.getDifficulty + 1).min(MAX_DIFFICULTY),
      level = gameSession.getGameState.currentLevel + 1
    ).build())

  override def spawn(pos: Option[Point2D]): ExitDoor = setPosition(pos)
