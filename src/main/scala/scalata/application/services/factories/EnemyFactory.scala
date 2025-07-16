package scalata.application.services.factories

import scalata.application.services.EntityFactory
import scalata.domain.entities.Enemy
import scalata.domain.util.EnemyClasses

import scala.util.Random

/** Factory responsible for creating every kind of [[Enemy]].
  *
  * <h4>Capabilities</h4> <ul> <li><b>`create`</b> – build a concrete enemy from
  * an [[EnemyClasses]] tag.</li> <li><b>`randomGeneration`</b> – convenience
  * helper that picks a random enemy family and delegates to
  * <code>create</code>.</li> </ul>
  *
  * The factory is <em>pure</em>: every call returns a fresh instance; no shared
  * state is mutated.
  */
class EnemyFactory extends EntityFactory[EnemyFactory, Enemy, EnemyClasses]:

  /** Generate a random enemy.
    *
    * @param id
    *   unique identifier to assign to the new enemy
    * @return
    *   freshly-constructed [[Enemy]]
    */
  def randomGeneration(id: String): Enemy =
    create(EnemyClasses.values(Random.nextInt(EnemyClasses.values.length)), id)

  /** Build the requested enemy type.
    *
    * @param entityType
    *   discriminator selecting Goblin / Pig
    * @param id
    *   unique identifier
    */
  override def create(entityType: EnemyClasses, id: String): Enemy =
    entityType match
      case EnemyClasses.Goblin =>
        Enemy(
          id = id,
          name = "Goblin",
          enemyType = entityType,
          health = 40,
          maxHealth = 40,
          attackPower = 10
        )
      case EnemyClasses.Pig =>
        Enemy(
          id = id,
          name = "Pig",
          enemyType = entityType,
          health = 60,
          maxHealth = 60,
          attackPower = 5
        )
