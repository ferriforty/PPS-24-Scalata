---
title: Detailed Design
layout: default
nav_order: 5
---

Here is the requested English translation.

# Detailed Design

This chapter outlines the detailed design choices implemented in the Scalata project, diving into the code-base 
organisation, the architectural patterns adopted, and the technical decisions that guided development.  
The system architecture follows **Hexagonal Architecture** principles combined with **Domain-Driven Design (DDD)**, ensuring a clear separation between business logic and I/O mechanisms.

## Package Structure

The code-base is divided into main packages that mirror the layers of the hexagonal architecture:

![plot](package.png)

### Package Overview

The Scalata project is divided into three concentric layers—**domain**, **application**, 
and **infrastructure**—mirroring a classic Hexagonal Architecture.  
Each layer is further split into sub-packages whose names describe their exact responsibility.

#### 1. `scalata.domain`

*The pure heart of the application—no I/O, no frameworks.*

* **`entities`**  
  *Fine-grained objects such as*  
  `Player`, `Enemy`, `Item`.  
  They expose intentful methods (`move`, `receiveDamage`, `useItem`) that return new instances, 
never mutating themselves.

* **`world`**  
  Aggregates that group several entities and guarantee invariants:  
  `Room` (geometry + occupants), `Floor` (connected rooms), `World` (vector of floors), and `GameSession` 
(the single source of truth for a running game).  
  Factory methods in each companion object ensure illegal states are un-representable.

* **`util`**  
  Small, pure utilities—coordinate math (`Point2D`), cardinal directions, random helpers, validation combinators.  
  Nothing here imports from any other layer.

#### 2. `scalata.application`

*Pure orchestration and workflow.*

* **`usecases`**  
  Each class models exactly **one** “verb” from the requirement specification. Examples:  
  `StartGame` seeds the first world, `MovePlayer` applies a direction and updates combat checks, 
`EnemyTurn` asks the AI for moves and updates the world accordingly.  
  All logic remains side-effect-free behind an effect type `F[_]`; this lets tests swap `IO` 
with a deterministic mock.

* **`services`**  
  Shared control logic that multiple use-cases rely on:  
  `FloorGenerator` builds deterministic levels from a seed; `GamePhaseService` 
runs the finite-state machine for *Menu → Playing → Game Over*; 
`EnemyAI` wraps the Prolog engine and transforms game state into AI decisions.

* **`ports`**  
  Pure traits that define both **inbound** (“driven”) and **outbound** (“driving”) interfaces.  
  Example:

  ```scala
  trait GameView[F[_], I]:
    def display[A](text: A): F[Unit]
    def getInput: F[I]
    def displayError[A](message: A): F[Unit]
    def clearScreen: F[Unit]
  ```

  The application layer depends only on these abstractions, never on concrete I/O.

#### 3. `scalata.infrastructure`

*Everything that touches the outside world.*

* **`controller`**  
  Translates user input into domain operations. `GameController` pulls commands from a `GameView`, 
invokes the right use-case, and feeds the updated session back to the view. 
`MenuController` does the same for start-up menus.

* **`view`**  
  Concrete UIs that implement `GameView`:

  * `ConsoleView`—ANSI terminal rendering with colourful ASCII art.
  * `TestView`—headless stub that returns scripted input for unit tests.
  * Future GUI adapters can be added here without touching the rest of the code-base.

### How the Layers Interact

1. **Infrastructure** receives an event (key press).
2. A **controller** maps the event to a **use-case** call.
3. The **use-case** pulls & updates **domain** objects, possibly invoking an application **service**.
4. The modified, *pure* `GameSession` is returned to the controller.
5. The **view** renders the new state.

Because dependencies flow strictly *inwards*, you can replace any outer package 
(e.g., swap `ConsoleView` for a GUI) without recompiling the core layers.


### Dynamic View – Typical Turn Sequence

![plot](../04_detailed_design/sequence_arch_sclata.png)





## Domain Layer

The domain layer contains the game’s core entities, all **immutable** and **side-effect-free**. 
Key entities include:

- **Player** – stores health, inventory, and position
- **Enemy** – AI-driven entity with movement and attack behaviour
- **Item** – collectible objects (weapons, potions, keys …)
- **Room** – rectangular areas delimited by walls and doors
- **World** – aggregate of every floor visited in the current run
- **GameSession** – immutable snapshot of the overall game state

```scala
// Example domain entities
case class Player(
  id: String,
  position: Point2D,
  health: Int,
  inventory: List[Item],
  equippedWeapon: Option[Weapon]
) extends Entity

case class Room(
  name: String,
  topLeft: Point2D,
  bottomRight: Point2D,
  connections: Map[Direction, String]
) extends Entity
```

### Application Layer

The application layer orchestrates domain entities through specific use-cases and services. Main components are:

- **GamePhaseService** – manages the game’s phases (menu, gameplay, game-over)
- **FloorGenerator** – responsible for procedural level generation
- **Use-Cases**
    - `GameRunningUseCase` – handles the main gameplay loop
    - `EnemyMovementUseCase` – coordinates AI movement
    - `CreatureUseCase` – manages interactions among creatures

### Infrastructure Layer

The infrastructure layer provides concrete implementations of the ports defined in the application layer:

- **GameView Implementations**
    - `ConsoleView` – text interface for the terminal
    - `TestView` – headless implementation for automated tests
- **Controllers**
    - `GameController` – main game controller
    - `MenuController` – menu management
    - Additional specialisations for specific game phases

## Architectural Patterns

### Hexagonal Architecture with DDD

Hexagonal Architecture isolates the domain from external dependencies. Outer layers depend on inner layers, 
respecting the **Dependency Inversion Principle**:

```text
Domain ← Application ← Infrastructure
```

Benefits include:

- Domain logic testable in isolation
- UI implementations easily swappable
- Business logic kept technology-agnostic

### Singleton Pattern for `FloorGenerator`

The floor-generation system uses the **Singleton** pattern to centralise generation logic:

```scala
object FloorGenerator:
  def generateFloor(player: Player, difficulty: Int, seed: Long): World =
    // Centralised, stateless generation logic
```

Advantages:

- Single, consistent entry point
- Stateless operations
- Memory-efficient (no duplicate instances)
- Stable interface for unit testing

### Monad Pattern with Cats Effect

The project leverages the **Monad** pattern via Cats Effect `IO` to handle side effects functionally:

```scala
trait GameView[F[_], I]:
  def display[A](text: A): F[Unit]
  def getInput: F[I]
  def displayError[A](message: A): F[Unit]
  def clearScreen: F[Unit]
```

*Effect polymorphism* through the `F[_]` type parameter lets the same code:

- Abstract over the effect type
- Use mock effects during tests
- Preserve functional purity
- Support multiple runtimes (`IO`, `SyncIO`, `State`, …)

### State Management with `NonEmptyList`

Game state history is kept in `cats.data.NonEmptyList` to guarantee an immutable, non-empty log of sessions:

```scala
def store: GameSession =
  copy(history = NonEmptyList(getSession, history.toList))

def undo: GameSession =
  history.tail.toNel.fold(
    updateGameState(getGameState.withNote(GameError.UndoError().message))
  ) { t =>
    copy(
      world = t.head._1,
      gameState = t.head._2,
      history = t
    )
  }
```

Key points:

- Immutable snapshot after every operation
- Compile-time safety (history is never empty)
- Simple, side-effect-free undo/redo
- Concurrency friendly when paired with `Ref[IO, GameSession]`



*Single-threaded execution* guarantees deterministic order; hot paths (floor generation, AI) stick to
immutable data to meet performance constraints.

## Prolog Integration for AI

### tuProlog Architecture

The system embeds **tuProlog** to drive enemy AI, keeping rule logic in Prolog and state mutation in Scala:

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

#### Scala Wrapper for tuProlog

The wrapper addresses several challenges:

- **LazyList** – streams solutions without excess memory
- **Exception safety** – avoids `NoMoreSolutionException` via careful engine control
- **Null safety** – relies on `Option` values
- **Memoisation** – leverages Scala laziness for performance

#### Dynamic Fact Creation

Scala game state is converted to Prolog facts each frame:

```scala
// From Scala
facts ++= s"grid_size(${w},${h})."
facts ++= s"player(pos(${px},${py}))."
facts ++= s"obstacle(pos(${ox},${oy}))."
facts ++= s"enemy(${id},pos(${ex},${ey}))."

// In Prolog
:- dynamic(distance/2, obstacle/1, enemy/2).
```

Each frame:

1. `retractall/1` removes previous distances
2. `assertz/2` inserts the new seed fact
3. `bfs/2` recursively asserts `distance(Pos, Cost)` for every reachable tile

#### Path-Finding Algorithm

A Prolog BFS ensures:

- **Completeness** – finds paths even in infinite spaces
- **Optimality** – always returns the shortest route
- **Memory efficiency** – queue-based exploration
- **Duplicate prevention** – tracks visited nodes

```prolog
bfs([], _).
bfs([Pos-D | Tail], Seen0) :-
    D1 is D + 1,
    findall(N-D1,
        (neigh(Pos, N),
         \+ memberchk(N, Seen0)),
        NewPairs),
    forall(member(P-C, NewPairs),
        (\+ distance(P, _) -> assertz(distance(P, C)); true)),
    append(Tail, NewPairs, Queue1),
    bfs(Queue1, Seen1).
```

#### Move-Extraction Pipeline

A three-step pipeline selects enemy moves:

1. **Candidate collection** (Prolog) – gathers all possible moves
2. **Move fetching** (Scala) – converts solutions into typed objects
3. **Grouping and selection** (Scala) – applies a priority system to avoid collisions

```scala
private def decideMoves(
  moves: Map[String, List[Point2D]]
): Map[String, Point2D] =
  val order = moves.toList.sortBy(_._2.size).map(_._1)
  val (_, chosen) =
    order.foldLeft((Set.empty[Point2D], Map.empty[String, Point2D])) {
      case ((reserved, steps), id) =>
        moves(id).find(!reserved(_)) match
          case Some(p) => (reserved + p, steps.updated(id, p))
          case None    => (reserved, steps)
    }
  chosen
```

The **fewest-options-first** strategy guarantees:

- Deadlock prevention – constrained enemies move first
- Fairness – balanced movement opportunities
- Minimal computational overhead

## Procedural Floor Generation

### Matrix Distribution

A functional approach lays out rooms on a grid:

```scala
val numRoomsFloor = (ROOMS.length + NUM_ROWS_DUNGEON - 1) / NUM_ROWS_DUNGEON
val shuffledRooms = Random.shuffle(ROOMS)
val matrixRooms: List[List[String]] = shuffledRooms.grouped(numRoomsFloor).toList
```

### Refactoring from Imperative to Functional

Initial index juggling with mutable variables was replaced by a functional pipeline:

```scala
// Imperative (before)
var j = 0
shuffledRooms.zipWithIndex.foreach((roomName, i) =>
  if i >= m * (j + 1) then j += 1
  // Complex index math...
)

// Functional (after)
(for
  (row, rowIndex)   0 =>
      matrixRooms(row).lift(col - 1).map(Direction.West -> _)
    case Direction.East =>
      matrixRooms(row).lift(col + 1).map(Direction.East -> _)
    case Direction.North if row > 0 =>
      matrixRooms.lift(row - 1).flatMap(_.lift(col)).map(Direction.North -> _)
    case Direction.South =>
      matrixRooms.lift(row + 1).flatMap(_.lift(col)).map(Direction.South -> _)
    case _ => None
  }.toMap
```

*Safe collection access* through `lift` prevents `IndexOutOfBoundsException`, returning `Option[T]` instead 
of throwing.

## Error Handling and Validation

### Monadic Error Management

Comprehensive error handling is achieved by monadic composition:

```scala
gameView.getInput.flatMap { raw =>
  parse(raw) match
    case Some(out) => Sync[F].pure(out)
    case None      => gameView.displayError("Try again!") *> run(gameView, parse)
}
```

Highlights:

- **Monadic recovery** – invalid input triggers recursive retry
- **Stack safety** – Cats Effect trampolining avoids stack overflows
- **Composable** – effects can be chained with recovery logic

### Validation Pattern

Strict type-driven validation through exhaustive pattern matching:

```scala
override protected def parse(raw: I): Option[PlayerClasses] =
  raw.toString.trim.toLowerCase match
    case "m" => Some(PlayerClasses.Mage)
    case "b" => Some(PlayerClasses.Barbarian)
    case "a" => Some(PlayerClasses.Assassin)
    case _   => None
```

## Game Loop and State Transitions

### Recursive Monadic Loops

The main game loop showcases advanced monadic composition:

```scala
final def gameLoop(
    gamePhaseService: GamePhaseService = GamePhaseService(),
    gameBuilder: GameBuilder           = GameBuilder(None),
    controllers: GameControllerState => Controller[F]
): F[ExitCode] =
  
  val controller = controllers(gamePhaseService.getCurrentPhase)

  controller
    .start(gameBuilder)
    .flatMap {
      case GameResult.Success((nextPhase, w), _) =>
        gameLoop(
          gamePhaseService.transitionTo(nextPhase),
          w,
          controllers
        )
      case GameResult.Error(_) =>
        ExitCode.Success.pure[F]
    }
```

Features:

- **Stack-safe recursive loops**
- **Effect polymorphism** – works with any `F[_]` that has a `Sync` instance
- **Composable state management** through monads

### Testing Infrastructure

Monad-based design enables thorough testing via effect substitution:

```scala
final class TestView(input: String) extends GameView[IO, String]:
  def display[A](text: A): IO[Unit]         = IO.unit
  def getInput: IO[String]                  = IO.pure(input)
  def displayError[A](message: A): IO[Unit] = IO.unit
  def clearScreen: IO[Unit]                 = IO.unit
```

Benefits:

- **Deterministic behaviour** – test views supply predictable input
- **Zero side effects** – tests run without console interaction
- **Effect verification** – assertions can inspect the effect structure

## Performance Optimisations

### Lazy Evaluation

Extensive use of Scala lazy evaluation boosts performance:

```scala
LazyList.unfold(Option(engine.solve(goal))) {
  case Some(info) if info.isSuccess =>
    val next = if info.hasOpenAlternatives then Some(engine.solveNext()) else None
    Some(info -> next)
  case _ => None
}
```

Advantages:

- **Bounded memory** – prevents out-of-memory errors
- **On-demand computation** – evaluates only what is needed
- **GC friendly** – efficient resource usage

### Memoisation and Caching

Memoisation via lazy evaluation and caching patterns yields:

- **Single-pass algorithms** – minimises redundant calculations
- **Stream processing** – memory-efficient pipelines
- **Cached results** – computed values reused where possible

## Conclusions

The detailed design of **Scalata** demonstrates a systematic application of functional-programming 
principles and modern architectures. Hexagonal Architecture coupled with DDD ensures clear separation 
of concerns, while Prolog integration for AI and Cats Effect for effect management showcase a mature 
functional approach.

Architectural choices make the system:

- **Testable** – domain isolation and effect polymorphism
- **Maintainable** – well-defined responsibilities
- **Extensible** – new features can be added without affecting the core
- **Performant** – optimisations via lazy evaluation and memoisation

The outcome is a system that balances functional elegance with practical requirements for performance 
and maintainability, providing a solid foundation for future game evolutions.
