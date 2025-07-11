package scalata.application.usecases.enemyusecases

import scalata.application.usecases.CreatureUseCase
import scalata.domain.world.{GameSession, Room}
import alice.tuprolog.*
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.Scala2P
import scalata.domain.util.Scala2P.{*, given}

class EnemyMovementUseCase extends CreatureUseCase[Room, Room]:
  override def execute(currentRoom: Room, gameSession: GameSession): Room =

    val rules: String =
      """
        |:- dynamic(distance/2, obstacle/2, enemy/2).
        |
        |memberchk(X,[Y|_])  :- X == Y, !.
        |memberchk(X,[_|Ys]) :- memberchk(X,Ys).
        |
        |
        |walkable(X,Y) :-
        |    number(X), number(Y), !,
        |    X >= 1, Y >= 1,
        |    grid_size(W,H),
        |    X < W, Y < H, !,
        |    \+ obstacle(X,Y).
        |
        |neigh(pos(X,Y), pos(X1,Y)) :- X1 is X+1, walkable(X1,Y).
        |neigh(pos(X,Y), pos(X1,Y)) :- X1 is X-1, walkable(X1,Y).
        |neigh(pos(X,Y), pos(X,Y1)) :- Y1 is Y+1, walkable(X,Y1).
        |neigh(pos(X,Y), pos(X,Y1)) :- Y1 is Y-1, walkable(X,Y1).
        |
        |free_neigh(From,To) :-
        |    neigh(From,To),
        |    \+ enemy(_,To).
        |
        |
        |build_distances(Start) :-
        |    retractall(distance(_,_)),
        |    assertz(distance(Start,0)),
        |    bfs([Start-0], [Start]).
        |
        |bfs([], _).
        |bfs([Pos-D | Tail], Seen0) :-
        |   D1 is D + 1,
        |   findall(N-D1,
        |     (neigh(Pos,N),
        |     \+ memberchk(N,Seen0)),
        |     NewPairs),
        |   forall(member(P-C, NewPairs),
        |   (\+ distance(P,_) -> assertz(distance(P,C));
        |     true)),
        |   extract_positions(NewPairs, NewNodes),
        |   append(Seen0, NewNodes, Seen1),
        |   append(Tail,  NewPairs, Queue1),
        |   bfs(Queue1, Seen1).
        |
        |extract_positions([], []).
        |extract_positions([P-_|T], [P|R]) :- extract_positions(T, R).
        |
        |best_moves_all(Moves) :-
        |    findall(move(Id, Cur, Next),
        |      ( enemy(Id, Cur),
        |        distance(Cur, D0),
        |        D0 > 1,
        |        D1 is D0 - 1,
        |        free_neigh(Cur, Next),
        |        distance(Next, D1)
        |      ),
        |      Closer),
        |    (  Closer \== []
        |    -> setof(M, member(M, Closer), Moves)
        |    ;
        |       findall(move(Id, Cur, Next),
        |         ( enemy(Id, Cur),
        |           distance(Cur, D0),
        |           D0 > 1,
        |           free_neigh(Cur, Next)
        |         ),
        |         Any),
        |       ( Any \== [] -> setof(M, member(M, Any), Moves)
        |       ;               Moves = []
        |       )
        |    ).
        |
        |""".stripMargin
    val playerPos = gameSession.getWorld.getPlayer.position
    val padding = currentRoom.topLeft
    val facts = createFacts(currentRoom, playerPos, padding)

    val engine: Term => LazyList[SolveInfo] = mkPrologEngine(facts + rules)
    val startPos = Term.createTerm(
      s"pos(${playerPos.x - padding.x},${playerPos.y - padding.y})"
    )

    val input = Struct("build_distances", startPos)
    engine(input).headOption
    val moves = fetchMoves(engine)
    val decidedMoves = decideMoves(moves)

    currentRoom.withEnemies(
      currentRoom.getAliveEnemies.map(e =>
        e.move(decidedMoves.get(e.id) match
          case Some(newPos) => newPos.moveBy(padding)
          case None         => e.position)
      )
    )

  private def createFacts(
      currentRoom: Room,
      playerPos: Point2D,
      padding: Point2D
  ): String =
    val facts = new StringBuilder
    val size = currentRoom.size

    facts ++= s"grid_size(${size._1},${size._2}).\n"
    facts ++= s"player(pos(${playerPos.x - padding.x},${playerPos.y - padding.y})).\n"

    currentRoom.items.foreach(i =>
      facts ++=
        s"obstacle(${i.position.get.x - padding.x},${i.position.get.y - padding.y}).\n"
    )

    currentRoom.getAliveEnemies.foreach(e =>
      facts ++=
        s"enemy(${e.id},pos(${e.position.x - padding.x},${e.position.y - padding.y})).\n"
    )

    facts ++= s"obstacle(${playerPos.x - padding.x},${playerPos.y - padding.y}).\n"
    facts.toString()

  private case class Move(id: String, cur: Point2D, next: Point2D)
  private def fetchMoves(engine: Term => LazyList[SolveInfo]): List[Move] =
    val mVar = Var("M")

    engine(Struct("best_moves_all", mVar)).headOption.fold(Nil): info =>
      val listMoves = info.getVarValue("M").asInstanceOf[Struct]
      if listMoves.isEmptyList then Nil
      else
        import scala.jdk.CollectionConverters.*
        listMoves
          .listIterator()
          .asScala
          .toList
          .map: t =>
            val mv = t.asInstanceOf[Struct]
            Move(
              mv.getArg(0).getTerm.toString,
              asPoint(mv.getArg(1).getTerm),
              asPoint(mv.getArg(2).getTerm)
            )

  private def decideMoves(moves: List[Move]): Map[String, Point2D] =

    val grouped: Map[String, List[Point2D]] =
      moves.groupMap(_.id)(_.next)

    val order = grouped.toList.sortBy(_._2.size).map(_._1)
    println(order)
    val (_, chosen) =
      order.foldLeft((Set.empty[Point2D], Map.empty[String, Point2D])):
        case ((reserved, acc), id) =>
          val firstFree = grouped(id).find(!reserved(_))
          firstFree
            .map(p => (reserved + p, acc.updated(id, p)))
            .getOrElse((reserved, acc))

    chosen
