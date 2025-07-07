package scalata.application.usecases.enemyusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.world.{GameSession, Room}
import alice.tuprolog.*
import scalata.domain.util.Scala2P.{*, given}

class EnemyMovementUseCase extends CreatureUseCase[Room, Room]:
  override def execute(param: Room, gameSession: GameSession): Room =

    val rules: String =
      """
        |walkable(X,Y) :-
        |    number(X), number(Y),
        |    X >= 1, Y >= 1,
        |    grid_size(W,H),
        |    X < W, Y < H,
        |    \+ obstacle(X,Y).
        |
        |neigh(pos(X,Y),pos(X1,Y)) :- X1 is X+1, walkable(X1,Y).
        |neigh(pos(X,Y),pos(X1,Y)) :- X1 is X-1, walkable(X1,Y).
        |neigh(pos(X,Y),pos(X,Y1)) :- Y1 is Y+1, walkable(X,Y1).
        |neigh(pos(X,Y),pos(X,Y1)) :- Y1 is Y-1, walkable(X,Y1).
        |
        |
        |""".stripMargin

    val size = param.size
    val player = gameSession.getWorld.getPlayer.position
    val padding = param.topLeft

    val facts = new StringBuilder
    facts ++= s"grid_size(${size._1},${size._2}).\n"
    facts ++= s"player(pos(${player.x - padding.x},${player.y - padding.y})).\n"

    param.items.foreach(i => facts ++=
      s"obstacle(${i.position.get.x - padding.x},${i.position.get.y - padding.y}).\n")

    /*param.getAliveEnemies.foreach(e => facts ++=
      s"enemy(a,pos(${e.position.x - padding.x},${e.position.y - padding.y})).\n")*/

    val engine: Term => LazyList[Term] = mkPrologEngine(facts.toString() + rules)
    println(player.toString() + " " + padding.toString())
    println(size)
    val input = Struct("neigh", s"pos(${player.x - padding.x}, ${player.y - padding.y})", Var())
    engine(input) map (extractTerm(_, 1)) foreach (println(_))

    param
