---
title: Prolog Integration
layout: default
parent: Implementation
---

# Prolog Integration for Enemy Game AI

## Overview
The Scalata codebase embeds a **tuProlog** engine to drive game-AI logic.  
Logic rules live in Prolog; state mutation, rendering, and I/O remain pure Scala.

## tuProlog Framework Architecture

### Technical Overview

tuProlog is a **lightweight, Java-based Prolog implementation** designed for integration 
with object-oriented languages. Key features include:

- **Minimal core**: Only 155KB containing essential Prolog engine components
- **Dynamic configurability**: Library-based extension system
- **Multi-paradigm support**: Seamless Java-Prolog integration
- **Deployability**: Single JAR file deployment


### Core Components

The tuProlog architecture consists of:

| Component | Description |
| :-- | :-- |
| `Prolog` | Main engine class for query execution |
| `Theory` | Container for facts and rules |
| `SolveInfo` | Result container with variable bindings |
| `Term` | Abstract representation of Prolog terms |
| `Library` | Extensible predicate collections |

## Scala Integration Implementation

### Wrapper Architecture

The conversation thread reveals a sophisticated Scala wrapper for tuProlog that addresses 
several critical challenges:

```scala
def mkPrologEngine(clauses: String*): Term => LazyList[SolveInfo] = {
  val engine = Prolog()
  engine.setTheory(Theory(clauses.mkString("\n")))
  
  goal =>
    LazyList.unfold(Option(engine.solve(goal))) {
      case Some(info) if info.isSuccess =>
        val next = if info.hasOpenAlternatives then Some(engine.solveNext()) else None
        Some(info -> next)
      case _ => None
    }
}
```

### Key Design Decisions

1. **LazyList Usage**: Provides memory-efficient streaming of solutions
2. **Exception Safety**: Prevents `NoMoreSolutionException` through careful state management
3. **Null Avoidance**: Uses `Option` types for safe value handling
4. **Memoization**: Leverages Scala's lazy evaluation for performance

## Breadth-First Search Implementation

### Algorithm Design

The implementation demonstrates a **reverse breadth-first search** for pathfinding:

```prolog
build_distances(Start) :-
    retractall(distance(_,_)),
    assertz(distance(Start,0)),
    bfs([Start-0], [Start]).

bfs([], _).
bfs([Pos-D | Tail], Seen0) :-
    D1 is D + 1,
    findall(N-D1,
        (neigh(Pos,N), 
        \+ memberchk(N,Seen0)),
        NewPairs),
    forall(member(P-C, NewPairs),
    (\+ distance(P,_) -> assertz(distance(P,C)); true)),
    extract_positions(NewPairs, NewNodes),
    append(Seen0, NewNodes, Seen1),
    append(Tail, NewPairs, Queue1),
    bfs(Queue1, Seen1).
```

### Algorithm Properties

The BFS implementation provides:

- **Completeness**: Finds solutions even in infinite spaces
- **Optimality**: Guarantees shortest paths
- **Memory efficiency**: Queue-based exploration
- **Duplicate prevention**: Visited node tracking

## Dynamic Fact Creation

### From Scala to Prolog

The helper `createFacts` serialises the current room into Prolog statements:

```scala
facts ++= s"grid_size(${w},${h})."
facts ++= s"player(pos(${px},${py}))."
facts ++= s"obstacle(pos(${ox},${oy}))."          // one per wall / item
facts ++= s"enemy(${id},pos(${ex},${ey}))."
```

These strings are concatenated with the rule set and loaded as a fresh **Theory** every tick, 
so the engine always works on the latest snapshot.

### Inside Prolog

```prolog
:- dynamic(distance/2, obstacle/1, enemy/2).

build_distances(Start) :-
    retractall(distance(_,_)),          % purge stale data
    assertz(distance(Start,0)),         % seed player tile
    bfs([Start-0], [Start]).            % fill the field
```

*What happens*

1. `:- dynamic(...)` tells tuProlog that the listed predicates can change at run time.
2. Each game frame:
    - `retractall/1` removes the previous distance field.
    - `assertz/2` adds the seed fact.
    - `bfs/2` recursively **asserts** `distance(Pos,Cost)` for every tile it reaches.
3. These dynamic facts serve as a shared cost map for all enemies in that frame.

## Move Extraction Pipeline

### 1 – Collect candidate moves (in Prolog)

```prolog
best_moves_all(Moves) :-
    player(P),
    findall(move(Id,Cur,Next,Cost),
      ( enemy(Id,Cur),
        \+ neigh(Cur,P),          % avoid entering the player's square
        neigh(Cur,Next),
        distance(Next,Cost)
      ),
    Moves).
```

The predicate walks each enemy’s four neighbours, attaches the pre-computed `Cost`, and returns a flat **Prolog list of `move/4` structures**.

### 2 – Fetch moves (in Scala)

```scala
val mVar = Var("M")
engine(Struct("best_moves_all", mVar)).headOption.foreach { info =>
  val rawList = info.getVarValue("M").asInstanceOf[Struct]
  rawList.listIterator().asScala.foreach { term =>
    val mv = term.asInstanceOf[Struct]
    moves ::= Move(
      id   = mv.getArg(0).getTerm.toString,
      cur  = asPoint(mv.getArg(1)),
      next = asPoint(mv.getArg(2)),
      cost = asInt(mv.getArg(3))
    )
  }
}
```

*Highlights*
- `SolveInfo` delivers the binding `M = [...]`.
- Standard tuProlog list iterators stream each `move/4` without converting the entire list to Scala first.
- Helper converters (`asPoint`, `asInt`) enforce type safety.

### 3 – Group & choose moves (Scala)

The implementation includes sophisticated coordination mechanisms:

```scala
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
```

`decideMoves` iterates enemies in constraint order, reserving tiles as it goes to eliminate collisions.


### Priority System

The "fewest-options-first" approach ensures:

- **Deadlock prevention**: Constrained enemies move first
- **Fairness**: Balanced movement opportunities
- **Efficiency**: Minimal computational overhead


## Technical Challenges and Solutions

### Exception Handling

The implementation addresses several critical exception scenarios:

1. **NoMoreSolutionException**: Prevented through careful stream management
2. **InvalidTheoryException**: Handled through proper syntax validation
3. **Variable Access Issues**: Resolved through type-safe wrappers

### Performance Optimizations

Key optimizations include:

- **Lazy evaluation**: Prevents unnecessary computation
- **Memoization**: Caches computed results
- **Stream processing**: Efficient memory usage
- **Single-pass algorithms**: Minimizes redundant calculations


## Evaluation and Results

### Performance Characteristics

The system demonstrates:

- **Scalability**: Handles large game worlds efficiently
- **Responsiveness**: Real-time pathfinding capabilities
- **Reliability**: Exception-safe operation
- **Maintainability**: Clean separation of concerns


### Memory Management

LazyList usage provides significant advantages:

- **Bounded memory**: Prevents out-of-memory errors
- **On-demand computation**: Calculates only needed elements
- **Garbage collection friendly**: Efficient resource utilization
