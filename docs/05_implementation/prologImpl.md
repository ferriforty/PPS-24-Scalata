---
title: FloorGeneration
layout: default
parent: Implementation
---

# Prolog Integration for Game AI: A Comprehensive University Report

## Introduction

Prolog, short for "PROgramming in LOGic," is a declarative logic programming language that has found 
significant application in artificial intelligence and game development[^1][^2]. 
This report examines the implementation of Prolog-based systems for game AI, specifically 
focusing on pathfinding algorithms and enemy behavior using the tuProlog framework integrated with Scala.

## Prolog Programming Language Overview

### Core Characteristics

Prolog is a **logic-based, declarative programming language** that fundamentally 
differs from imperative languages[^1][^2]. Key characteristics include:

- **Declarative paradigm**: Programs specify what problems to solve rather than how to solve them[^3]
- **Pattern matching**: Sophisticated unification mechanism for term comparison[^4][^5]
- **Backtracking**: Automatic exploration of solution alternatives[^6]
- **Horn clauses**: Based on first-order logic principles[^2]


### Unification Mechanism

Unification is the core mechanism in Prolog that enables pattern matching and 
variable binding[^7][^4]. When two terms are unified, Prolog finds substitutions that make them identical:

```prolog
% Example unification
foo(X, b) = foo(a, Y)
% Results in: X = a, Y = b
```

The unification process involves[^4]:

1. Comparing term structures
2. Binding variables to appropriate values
3. Ensuring consistent substitutions across all variables

## tuProlog Framework Architecture

### Technical Overview

tuProlog is a **lightweight, Java-based Prolog implementation** designed for integration 
with object-oriented languages[^8][^9]. Key features include:

- **Minimal core**: Only 155KB containing essential Prolog engine components[^10]
- **Dynamic configurability**: Library-based extension system[^9]
- **Multi-paradigm support**: Seamless Java-Prolog integration[^11]
- **Deployability**: Single JAR file deployment[^8]


### Core Components

The tuProlog architecture consists of[^12][^11]:


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

1. **LazyList Usage**: Provides memory-efficient streaming of solutions[^13][^14]
2. **Exception Safety**: Prevents `NoMoreSolutionException` through careful state management
3. **Null Avoidance**: Uses `Option` types for safe value handling
4. **Memoization**: Leverages Scala's lazy evaluation for performance[^15]

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
        (neigh(Pos,N), \+ memberchk(N,Seen0)),
        NewPairs),
    forall(member(P-C, NewPairs),
        (\+ distance(P,_) -> assertz(distance(P,C)); true)),
    extract_positions(NewPairs, NewNodes),
    append(Seen0, NewNodes, Seen1),
    append(Tail, NewPairs, Queue1),
    bfs(Queue1, Seen1).
```


### Algorithm Properties

The BFS implementation provides[^16][^17]:

- **Completeness**: Finds solutions even in infinite spaces
- **Optimality**: Guarantees shortest paths
- **Memory efficiency**: Queue-based exploration
- **Duplicate prevention**: Visited node tracking


## Game AI Application

### Enemy Pathfinding System

The system implements intelligent enemy behavior through:

1. **Distance Field Generation**: Creates cost maps from player position
2. **Optimal Move Selection**: Chooses moves that minimize distance to player
3. **Collision Avoidance**: Prevents enemy overlap through reservation system
4. **Dynamic Updates**: Recalculates paths based on game state changes

### Multi-Enemy Coordination

The implementation includes sophisticated coordination mechanisms:

```scala
def decideMoves(moves: List[Move]): Map[String, Point2D] = {
  val grouped = moves.groupMap(_.id)(_.next)
  val order = grouped.toList.sortBy(_._2.size).map(_._1)
  
  val (_, chosen) = order.foldLeft((Set.empty[Point2D], Map.empty[String, Point2D])) {
    case ((reserved, acc), id) =>
      val firstFree = grouped(id).find(!reserved(_))
      firstFree
        .map(p => (reserved + p, acc.updated(id, p)))
        .getOrElse((reserved, acc))
  }
  chosen
}
```


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

- **Lazy evaluation**: Prevents unnecessary computation[^13]
- **Memoization**: Caches computed results[^14]
- **Stream processing**: Efficient memory usage[^13]
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


## Future Directions

### Potential Enhancements

1. **Parallel Processing**: Multi-threaded enemy calculations
2. **Machine Learning Integration**: Adaptive behavior patterns
3. **Dynamic Difficulty**: Skill-based pathfinding adjustments
4. **Advanced Coordination**: Flocking and formation behaviors

### Research Opportunities

- **Hybrid algorithms**: Combining A* with Prolog reasoning
- **Temporal logic**: Time-based behavior modeling
- **Constraint satisfaction**: Complex multi-objective optimization


## Conclusion

The integration of Prolog with Scala for game AI development demonstrates the powerful 
combination of declarative logic programming with functional programming paradigms. 
The tuProlog framework provides an excellent foundation for this integration, 
offering lightweight deployment and seamless interoperability.

The breadth-first search implementation showcases Prolog's strength in graph traversal 
and pathfinding applications, while the Scala wrapper ensures type safety and performance optimization. 
The resulting system provides intelligent, coordinated enemy behavior suitable for real-time game environments.

Key contributions of this work include:

- **Robust integration pattern** for Prolog-Scala interoperability
- **Exception-safe streaming** of Prolog solutions
- **Efficient pathfinding algorithms** for game AI
- **Coordinated multi-agent behavior** systems

This approach opens new possibilities for AI development in games, combining the expressiveness 
of logic programming with the performance characteristics of modern functional languages.

## References

[^1] Built In. "Introduction to Prolog: A Programming Language for AI"
[^8] University of Bologna. "A Java-based Prolog System for Pervasive Intelligence"
[^16] GitHub. "Prolog-Based Graph Traversal: BFS Implementation"
[^2] Wikipedia. "Prolog"
[^10] AMS Acta. "tuProlog Manual"
[^18] YouTube. "Breadth First Search in Prolog"
[^3] University of West Bohemia. "An Introduction to Prolog Programming"
[^9] DEIS Bologna. "tuProlog: A Light-Weight Prolog for Internet Applications"
[^19] GitHub. "bfs-prolog: Breadth-first search Prolog-like tool"
[^20] Tutorialspoint. "Prolog Introduction"
[^12] Javadoc.io. "Package alice.tuprolog"
[^17] University of Cluj. "Graphs Search Algorithms"
[^21] Mark Volkmann. "Prolog"
[^22] SourceForge. "tuProlog Guide"
[^23] Simply Logical. "Breadth-first search"
[^6] Imperial College London. "Prolog (1)"
[^24] AMS Acta. "tuProlog Manual"
[^25] UMBC. "Prolog Search"
[^26] University of Catania. "Introduction to Prolog Programming"
[^27] GitHub. "BasicLibrary.java"
[^28] YouTube. "Prolog vs Scala integration using tuProlog api"
[^29] HyperGraphDB. "SolveInfo API"
[^30] YouTube. "Enemy AI with Pathfinding"
[^31] SlideShare. "Towards Logic Programming as a Service"
[^32] Javatips. "LibraryTestCase.java example"
[^33] GameMaker Community. "Enemy AI Pathfinding"
[^34] Stack Overflow. "Embedded Prolog Interpreter/Compiler for Java"
[^35] Stack Overflow. "Error executing query in tuProlog"
[^36] YouTube. "GameMaker PATHFINDING AI Tutorial"
[^11] Science Direct. "Multi-paradigm Java–Prolog integration in tuProlog"
[^37] Toolify. "Master the Art of Enemy AI Pathfinding"
[^38] CEUR. "Towards Logic Programming as a Service"
[^39] Stack Overflow. "Prolog/tuprolog string parsing issues"
[^40] YouTube. "2D PATHFINDING - Enemy AI in Unity"
[^41] GitHub. "PPS-22-Prolog-as-scalaDSL"
[^42] Stack Overflow. "tuprolog and Definite Clause Grammars"
[^43] Stack Overflow. "Randomize AI pathfinding in a game"
[^44] Stack Overflow. "Using Prolog in Scala for type class instances"
[^7] AGH University. "Prolog Unification"
[^13] Lunatech. "Streams in Scala - An Introductory Guide"
[^4] University of Edinburgh. "Unification"
[^45] Stack Overflow. "Breadth-First in Prolog"
[^15] Stack Overflow. "Scala Stream tail laziness and synchronization"
[^5] Learn Prolog Now. "Unification"
[^14] Scala Documentation. "LazyList"
[^46] University of Iowa. "Substitutions and Unification in Prolog"
[^47] Rock the JVM. "ZIO Streams: A Long-Form Introduction"
[^48] Kyle Dewey. "Unification"
[^49] Scala Users. "Why is my implementation of Iterable not lazy"
[^50] YouTube. "Prolog 5: Unification"
[^51] UPCommons. "A partial breadth-first execution model for Prolog"
[^52] EPFL. "LazyList - Scala 3"
[^53] Amzi! inc. "Unification"
[^54] Stack Overflow. "Breadth First Search in Prolog"

<div style="text-align: center">⁂</div>

[^1]: https://builtin.com/software-engineering-perspectives/prolog

[^2]: https://en.wikipedia.org/wiki/Prolog

[^3]: https://www.kiv.zcu.cz/studies/predmety/uzi/Folie_Prolog/Intro_to_Prolog_prog.pdf

[^4]: https://www.dai.ed.ac.uk/groups/ssp/bookpages/quickprolog/node12.html

[^5]: https://lpn.swi-prolog.org/lpnpage.php?pagetype=html\&pageid=lpn-htmlse5

[^6]: https://www.doc.ic.ac.uk/~cclw05/topics1/prolog.html

[^7]: https://home.agh.edu.pl/~ligeza/wiki/doku.php?id=prolog%3Aunification

[^8]: https://lia.disi.unibo.it/corsi/2003-2004/SD-LS-CE/pdf/08-tuProlog.pdf

[^9]: http://lia.deis.unibo.it/~ao/pubs/pdf/2001/padl.pdf

[^10]: https://amsacta.unibo.it/5450/7/tuprolog-guide.pdf

[^11]: http://lia.deis.unibo.it/~ao/pubs/pdf/2005/scico-dor.pdf

[^12]: https://javadoc.io/static/it.unibo.alice.tuprolog/tuprolog/3.3.0/alice/tuprolog/package-summary.html

[^13]: https://blog.lunatech.com/posts/2023-07-28-streams-in-scala--an-introductory-guide

[^14]: https://www.scala-lang.org/api/3.x/scala/collection/immutable/LazyList.html

[^15]: https://stackoverflow.com/questions/48111320/scala-stream-tail-laziness-and-synchronization

[^16]: https://github.com/SreecharanV/Prolog-Based-Graph-Traversal-BFS-Implementation

[^17]: http://users.utcluj.ro/~cameliav/lp/11_Graphs_Search.pdf

[^18]: https://www.youtube.com/watch?v=xH-AZonBywI

[^19]: https://github.com/shiatsumat/bfs-prolog

[^20]: https://www.tutorialspoint.com/prolog/prolog_introduction.htm

[^21]: https://mvolkmann.github.io/blog/prolog/?v=1.1.1

[^22]: https://tuprolog.sourceforge.net/doc/2p-guide.pdf

[^23]: https://book.simply-logical.space/src/text/2_part_ii/5.3.html

[^24]: https://amsacta.unibo.it/3451/1/tuprolog-guide.pdf

[^25]: https://courses.cs.umbc.edu/771/current/presentations/prolog search.pdf

[^26]: https://www.dmi.unict.it/barba/PROG-LANG/PROGRAMMI-TESTI/READING-MATERIAL/intro-prolog-II.pdf

[^27]: https://github.com/hypergraphdb/prolog/blob/master/src/java/alice/tuprolog/lib/BasicLibrary.java

[^28]: https://www.youtube.com/watch?v=ShRHGTk7dgs

[^29]: https://hypergraphdb.org/docs/apps/prolog/alice/tuprolog/SolveInfo.html

[^30]: https://www.youtube.com/watch?v=mwi0pQB9Eog

[^31]: https://www.slideshare.net/slideshow/towards-logic-programming-as-a-service/64507148

[^32]: https://www.javatips.net/api/tuprolog-5-master/test/unit/alice/tuprolog/LibraryTestCase.java

[^33]: https://forum.gamemaker.io/index.php?threads%2Fenemy-ai-pathfinding.91072%2F

[^34]: https://stackoverflow.com/questions/1817010/embedded-prolog-interpreter-compiler-for-java

[^35]: https://stackoverflow.com/questions/3741152/error-executing-query-in-tuprolog

[^36]: https://www.youtube.com/watch?v=j-xSl-EOzm8

[^37]: https://www.toolify.ai/ai-news/master-the-art-of-enemy-ai-pathfinding-in-game-development-1591538

[^38]: https://ceur-ws.org/Vol-1664/w14.pdf

[^39]: https://stackoverflow.com/questions/17784581/prolog-tuprolog-a-single-line-that-returns-the-enitire-sic-string-could-not

[^40]: https://www.youtube.com/watch?v=jvtFUfJ6CP8

[^41]: https://github.com/kelvindev15/PPS-22-Prolog-as-scalaDSL

[^42]: https://stackoverflow.com/questions/16541383/tuprolog-and-definite-clause-grammars

[^43]: https://stackoverflow.com/questions/10485913/randomize-ai-pathfinding-in-a-game

[^44]: https://stackoverflow.com/questions/26723337/using-the-prolog-in-scala-to-find-available-type-class-instances

[^45]: https://stackoverflow.com/questions/769658/breadth-first-in-prolog/771398

[^46]: https://homepage.cs.uiowa.edu/~fleck/unification.pdf

[^47]: https://rockthejvm.com/articles/zio-streams-introduction

[^48]: https://kyledewey.github.io/comp410-fall21/lecture/week_7/unification_handout.pdf

[^49]: https://users.scala-lang.org/t/why-is-my-implementation-of-iterable-not-lazy/2744

[^50]: https://www.youtube.com/watch?v=ArOS4ZGfnog

[^51]: https://upcommons.upc.edu/bitstream/handle/2117/101360/00346504.pdf

[^52]: https://dotty.epfl.ch/api/scala/collection/immutable/LazyList.html

[^53]: https://www.amzi.com/AdventureInProlog/a10unif.php

[^54]: https://stackoverflow.com/questions/34082799/breadth-first-search-in-prolog

