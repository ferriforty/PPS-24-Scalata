package scalata.application.usecases.enemyusecases

import alice.tuprolog.*
import scalata.application.usecases.CreatureUseCase
import scalata.domain.util.Geometry.Point2D
import scalata.domain.util.Scala2P
import scalata.domain.util.Scala2P.{*, given}
import scalata.domain.world.{GameSession, Room}

class EnemyMovementUseCase extends CreatureUseCase[Room, Room]:
  override def execute(currentRoom: Room, gameSession: GameSession): Room =

    val rules: String =
      """
        |:- dynamic(distance/2, obstacle/1, enemy/2).
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
        |    \+ obstacle(pos(X,Y)).
        |
        |neigh(pos(X,Y), pos(X1,Y)) :- X1 is X+1, walkable(X1,Y).
        |neigh(pos(X,Y), pos(X1,Y)) :- X1 is X-1, walkable(X1,Y).
        |neigh(pos(X,Y), pos(X,Y1)) :- Y1 is Y+1, walkable(X,Y1).
        |neigh(pos(X,Y), pos(X,Y1)) :- Y1 is Y-1, walkable(X,Y1).
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
        |   player(P),
        |   findall(move(Id, Cur, Next, Cost),
        |     (
        |       enemy(Id, Cur),
        |       \+ neigh(Cur, P),
        |       neigh(Cur, Next),
        |       distance(Next, Cost)
        |    ),
        |    Moves).
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

    val decidedMoves = decideMoves(
      groupMoves(
        fetchMoves(engine)
      ).filterNot((_, m) => m.contains(playerPos))
    )

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
        s"obstacle(pos(${i.position.get.x - padding.x},${i.position.get.y - padding.y})).\n"
    )

    currentRoom.getAliveEnemies.foreach(e =>
      facts ++=
        s"enemy(${e.id},pos(${e.position.x - padding.x},${e.position.y - padding.y})).\n" +
          s"obstacle(pos(${e.position.x - padding.x},${e.position.y - padding.y})).\n"
    )

    facts ++= s"distance(pos(${playerPos.x - padding.x},${playerPos.y - padding.y}), 0).\n"
    facts.toString()

  private case class Move(
      id: String,
      cur: Point2D,
      next: Point2D,
      cost: scala.Int
  )

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
              asPoint(mv.getArg(2).getTerm),
              asInt(mv.getArg(3).getTerm)
            )

  private def groupMoves(moves: List[Move]): Map[String, List[Point2D]] =
    moves
      .groupMap(_.id)(m => (m.next, m.cost))
      .map((id, moves) =>
        val min = moves.map(_._2).min
        id -> moves.filter(_._2.equals(min)).map(_._1)
      )

  private def decideMoves(
      moves: Map[String, List[Point2D]]
  ): Map[String, Point2D] =
    val order = moves.toList.sortBy(_._2.size).map(_._1)
    val (_, chosen) =
      order.foldLeft((Set.empty[Point2D], Map.empty[String, Point2D])):
        case ((reserved, steps), id) =>
          moves(id).find(!reserved(_)) match
            case Some(p) => (reserved + p, steps.updated(id, p))
            case None    => (reserved, steps)

    chosen
