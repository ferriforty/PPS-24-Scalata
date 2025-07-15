package scalata.domain.entities

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalata.application.services.factories.EnemyFactory
import scalata.domain.entities.components.{Alive, Movable}
import scalata.domain.util.EnemyClasses

class EnemyTest extends AnyFlatSpec with Matchers:

  def makeEnemy(): Enemy = EnemyFactory().create(EnemyClasses.Goblin)

  "Enemy" should "be movable" in:
    val enemy = makeEnemy()
    enemy shouldBe a[Movable[Enemy]]

  "Enemy" should "be Alive" in:
    val enemy = makeEnemy()
    enemy shouldBe a[Alive[Enemy]]

  "Enemy" should "take damage" in:
    val enemy = makeEnemy()
    val damage = 30

    enemy.takeDamage(damage).health shouldBe (enemy.health - damage)

  "Enemy" should "heal" in:
    val enemy = makeEnemy()
    val damage = 20
    val heal1 = 60
    val heal2 = 10

    enemy.takeDamage(damage).heal(heal1).health shouldBe enemy.maxHealth
    enemy
      .takeDamage(damage)
      .heal(heal2)
      .health shouldBe (enemy.health - damage + heal2)

  "Enemy" should "die" in:
    val enemy = makeEnemy()
    enemy.isAlive shouldBe true
    enemy.takeDamage(enemy.maxHealth + 1).isAlive shouldBe false
