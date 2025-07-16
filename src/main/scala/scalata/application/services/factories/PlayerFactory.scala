package scalata.application.services.factories

import scalata.application.services.EntityFactory
import scalata.domain.entities.Player
import scalata.domain.util.PlayerClasses

/** Factory that creates a fresh [[Player]] for the requested
  * [[scalata.domain.util.PlayerClasses]].
  *
  * The factory is <i>pure</i>: every call returns a brand-new instance and does
  * not cache or mutate shared state.
  */
class PlayerFactory extends EntityFactory[PlayerFactory, Player, PlayerClasses]:

  /** Build a player of the specified archetype.
    *
    * @param playerClass
    *   hero archetype to instantiate
    * @param id
    *   unique identifier (defaults to <code>"p1"</code>)
    * @return
    *   freshly constructed [[Player]]
    */
  def create(playerClass: PlayerClasses, id: String = "p1"): Player =
    playerClass match
      case PlayerClasses.Mage =>
        Player(
          id = id,
          role = playerClass,
          health = 100,
          maxHealth = 100,
          attackPower = 10
        )
      case PlayerClasses.Barbarian =>
        Player(
          id = id,
          role = playerClass,
          health = 100,
          maxHealth = 100,
          attackPower = 15
        )
      case PlayerClasses.Assassin =>
        Player(
          id = id,
          role = playerClass,
          health = 100,
          maxHealth = 100,
          attackPower = 10
        )
