package scalata.application.usecases.enemyusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.world.{GameSession, Room}
import alice.tuprolog.*
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.Scala2P
import scalata.domain.util.Scala2P.{*, given}

class EnemyMovementUseCase extends CreatureUseCase[Room, Room]:
  override def execute(param: Room, gameSession: GameSession): Room =

    val rules: String =
      """
        |memberchk(X,[Y|_])  :- X == Y, !.
        |memberchk(X,[_|Ys]) :- memberchk(X,Ys).
        |
        |
        |walkable(X,Y) :-
        |   number(X), number(Y), !,
        |   X >= 1, Y >= 1,
        |   grid_size(W,H),
        |   X < W, Y < H, !,
        |   \+ obstacle(X,Y).
        |
        |neigh(pos(X,Y), pos(X1,Y)) :- X1 is X+1, walkable(X1,Y).
        |neigh(pos(X,Y), pos(X1,Y)) :- X1 is X-1, walkable(X1,Y).
        |neigh(pos(X,Y), pos(X,Y1)) :- Y1 is Y+1, walkable(X,Y1).
        |neigh(pos(X,Y), pos(X,Y1)) :- Y1 is Y-1, walkable(X,Y1).
        |
        |
        |build_distances(Start) :-
        |   retractall(distance(_,_)),
        |   assertz(distance(Start,0)),
        |   bfs([Start-0], [Start]).
        |
        |bfs([], _).
        |bfs([Pos-D | Tail], Seen0) :-
        |   D1 is D + 1,
        |   findall(
        |     N-D1,
        |     (neigh(Pos,N),
        |       \+ memberchk(N,Seen0)),
        |     NewPairs
        |   ),
        |   forall(member(P-C, NewPairs),
        |     (\+ distance(P,_) -> assertz(distance(P,C)); true)),
        |
        |   extract_positions(NewPairs, NewNodes),
        |   append(Seen0, NewNodes, Seen1),
        |   append(Tail,  NewPairs, Queue1),
        |   bfs(Queue1, Seen1).
        |
        |extract_positions([], []).
        |extract_positions([P-_|T], [P|R]) :- extract_positions(T, R).
        |
        |best_step(Id, Next) :-
        |   enemy(Id, Cur),
        |   distance(Cur, D0),
        |   D1 is D0 - 1,
        |   neigh(Cur, Next),
        |   distance(Next, D1).
        |""".stripMargin

    val size = param.size
    val player = gameSession.getWorld.getPlayer.position
    val padding = param.topLeft

    val facts = new StringBuilder
    facts ++= s"grid_size(${size._1},${size._2}).\n"
    facts ++= s"player(pos(${player.x - padding.x},${player.y - padding.y})).\n"

    param.items.foreach(i => facts ++=
      s"obstacle(${i.position.get.x - padding.x},${i.position.get.y - padding.y}).\n")

    param.getAliveEnemies.foreach(e => facts ++=
      s"enemy(${e.id},pos(${e.position.x - padding.x},${e.position.y - padding.y})).\n")

    val engine: Term => LazyList[Term] = mkPrologEngine(facts.toString() + rules)
    val startPos = Term.createTerm(s"pos(${player.x - padding.x},${player.y - padding.y})")

    val input = Struct("build_distances", startPos)
    engine(input) map (extractTerm(_, 0)) foreach (println(_))

    param.withEnemies(
      param.getAliveEnemies.map(e =>
        e.move(
          bestStep(
            Term.createTerm(s"pos(${e.position.x - padding.x},${e.position.y - padding.y})"),
            engine) match
              case Some(newPos) => newPos.moveBy(padding)
              case None => e.position
        )
      )
    )

  private def bestStep(enemyPos: Term, engine: Term => LazyList[Term]): Option[Point2D] =
    val q = Struct(
      "neigh",
      enemyPos,
      Var("N")
    )

    engine(q).find: sol =>
      val n = extractTerm(sol, 1)
      val cur = extractTerm(engine(Struct("distance", enemyPos, Var("D"))).head, 1).toString.toInt
      val nxt = engine(Struct("distance", n, Var("D"))).headOption
        .map(extractTerm(_, 1).toString.toInt)
      nxt.contains(cur - 1)

    .map(t =>
      val pos = extractTerm(t, 1)
      Point2D(extractTerm(pos, 0).toString.toInt, extractTerm(pos, 1).toString.toInt)
    )
