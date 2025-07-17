---
title: Monad Implementation
layout: default
parent: Implementations
---

# Monad Implementation in Scalata Game Project

## Overview

The Scalata project leverages **Cats Effect's IO monad** to achieve pure functional programming 
with referential transparency while maintaining clean architecture principles. 
The implementation demonstrates advanced Scala 3 features and effect-polymorphic design patterns 
that separate pure business logic from side effects.

## Core Monad Architecture

### Cats Effect IO Monad

The project is built around **Cats Effect's IO monad**, which provides:

- **Pure effect descriptions**: `IO[A]` represents a computation that, when executed, performs effects and yields a value of type `A`
- **Referential transparency**: Effects are described as values, not executed immediately
- **Composability**: Effects can be sequenced, transformed, and combined using monadic operations
- **Resource safety**: Built-in support for resource management and cancellation

```scala
import cats.effect.{ExitCode, IO, IOApp}
import scalata.domain.util.GameControllerState
import scalata.infrastructure.controller.GameEngine
import scalata.infrastructure.view.terminal.Shared

object CliApp extends IOApp:

  private val view = ConsoleView[IO]()

  def run(args: List[String]): IO[ExitCode] =
    GameEngine[IO, String]()
      .gameLoop(controllers = Shared.getControllersMap[IO, String](view))
```


### Effect Polymorphism with GameView

The cornerstone of the monad implementation is the **GameView trait**, designed with effect polymorphism to abstract over different effect types:

```scala
trait GameView[F[_], I]:
  def display[A](text: A): F[Unit]
  def getInput: F[I]
  def displayError[A](message: A): F[Unit]
  def clearScreen: F[Unit]
```

This design enables:

- **Technology agnostic interfaces**: The same port works across different UI implementations
- **Testability**: Mock implementations can use `SyncIO` or `State` monads
- **Flexibility**: Production code uses `IO`, while tests can use deterministic effects


### Concrete Implementation with Sync

The console implementation demonstrates proper effect capture using the `Sync` typeclass:

```scala
final class ConsoleView[F[_]: Sync] extends GameView[F, String]:
  override def display[String](text: String): F[Unit] =
    clearScreen *> Sync[F].delay(println(text))
    
  override def getInput: F[String] =
    Sync[F].blocking(Option(scala.io.StdIn.readLine()).getOrElse("").trim)
    
  override def displayError[String](msg: String): F[Unit] =
    Sync[F].delay(println(s"Error: $msg"))
    
  override def clearScreen: F[Unit] =
    Sync[F].delay(print("\u001b[2J\u001b[H"))
```

Key implementation details:

- **`Sync[F].delay`**: Captures synchronous side effects (printing to console)
- **`Sync[F].blocking`**: Executes blocking operations on dedicated thread pool
- **Defensive programming**: Handles `null` returns from `readLine()` safely
- **Effect composition**: Uses `*>` operator to sequence effects


## Advanced Monad Patterns

### State management with `NonEmptyList`

The game session keeps a *versioned* history inside an immutable `cats.data.NonEmptyList`, 
itself a lawful **Monad**. Each operation returns a fresh `GameSession`; no mutation occurs.

```scala
// Save current snapshot to the head of the history
def store: GameSession =
  copy(history = NonEmptyList(getSession, history.toList))

// Revert to the previous snapshot (or add an UndoError note if none remain)
def undo: GameSession =
  history.tail.toNel.fold(
    updateGameState(getGameState.withNote(GameError.UndoError().message))
  ){ t =>
    copy(
      world     = t.head._1,
      gameState = t.head._2,
      history   = t
    )
  }
```

#### Why this works

- **Monad–friendly** `NonEmptyList`’s `flatMap` lets you chain `store`, `undo`, and custom transforms in a for-comprehension.
- **Immutable snapshots** Every call produces a new value; earlier states stay intact.
- **Compile-time safety** History can never be empty, eliminating “empty stack” runtime errors.
- **Simple undo/redo** `store` pushes, `undo` pops—pure, side-effect-free logic.
- **Concurrency ready** Wrap the whole session in a `Ref[IO, GameSession]` to make updates atomic when multiple fibers are involved.

### Error Handling and Validation

The monad implementation includes comprehensive error handling:

```scala
gameView.getInput.flatMap: raw =>
  parse(raw) match
    case Some(out) => Sync[F].pure(out)
    case None => gameView.displayError("Try again!") *> run(gameView, parse)

override protected def parse(raw: I): Option[PlayerClasses] =
  raw.toString.trim.toLowerCase match
    case "m" => Some(PlayerClasses.Mage)
    case "b" => Some(PlayerClasses.Barbarian)
    case "a" => Some(PlayerClasses.Assassin)
    case _   => None
```

Features:

- **Monadic error recovery**: Invalid input triggers recursive retry
- **Stack safety**: Cats Effect's trampolining prevents stack overflow
- **Composable error handling**: Effects can be chained with error recovery


### Game Loop Implementation

The main game loop demonstrates advanced monadic composition:

```scala
class GameEngine[F[_]: Sync, I]:
    final def gameLoop(
        gamePhaseService: GamePhaseService = GamePhaseService(),
        gameBuilder: GameBuilder = GameBuilder(None),
        controllers: GameControllerState => Controller[F]
    ): F[ExitCode] =
      
      val controller = controllers(gamePhaseService.getCurrentPhase)
    
      controller
        .start(gameBuilder)
        .flatMap:
      case GameResult.Success((nextPhase, w), _) =>
        gameLoop(
          gamePhaseService.transitionTo(nextPhase),
          w,
          controllers
        )
      case GameResult.Error(_) => ExitCode.Success.pure[F]
```

This pattern shows:

- **Recursive monadic loops**: Stack-safe game state transitions
- **Effect polymorphism**: Works with any `F[_]` that has a `Sync` instance


## Domain-Driven Design Integration

### Hexagonal Architecture Compliance

The monad implementation respects DDD principles:

```
Domain Layer (Pure)
├── GameState, Player, Move (no effects)
└── Business rules (pure functions)

Application Layer (Effect-Polymorphic)
├── GameView[F[_]] (port definition)
├── Use cases return F[Result]
└── Effect orchestration

Infrastructure Layer (Concrete Effects)
├── ConsoleView extends GameView[IO]
├── TestView extends GameView[SyncIO]
└── Platform-specific implementations
```


### Benefits of This Architecture

1. **Pure Domain**: Core business logic has no dependency on effects
2. **Testable Application Layer**: Effect polymorphism enables easy mocking
3. **Flexible Infrastructure**: Multiple UI implementations possible
4. **Technology Independence**: Can swap effect systems without domain changes

## Testing Infrastructure

The monad design enables comprehensive testing:

```scala
final class TestView(input: String) extends GameView[IO, String]:
  def display[A](text: A): IO[Unit] = IO.unit
  def getInput: IO[String] = IO.pure(input)
  def displayError[A](message: A): IO[Unit] = IO.unit
  def clearScreen: IO[Unit] = IO.unit
```

Testing benefits:

- **Deterministic behavior**: Test views provide predictable input
- **No side effects**: Tests run without console interaction
- **Effect verification**: Can assert on the structure of effect computations


## Key Achievements

The monad implementation in Scalata demonstrates:

1. **Functional Purity**: All side effects are properly isolated and managed
2. **Effect Polymorphism**: Clean abstraction over different effect types
3. **Resource Safety**: Proper handling of console I/O and system resources
4. **Composability**: Complex game logic built from simple, composable effects
5. **Testability**: Comprehensive testing through effect substitution

This implementation serves as a practical example of how modern functional programming 
techniques can be applied to game development while maintaining clean architecture principles 
and comprehensive error handling.

