---
title: Floor Generation
layout: default
parent: Implementations
---

# Floor Generation System Implementation

## Overview

This section details the implementation of the floor generation system for the Scalata game project,
focusing on the singleton pattern implementation and functional programming approaches used in Scala.
The system is responsible for creating game floors with randomized room arrangements, proper connectivity,
and deterministic generation capabilities.

## Architectural Decision: Singleton Pattern

The floor generation functionality is implemented as a singleton object in Scala (`FloorGenerator`),
providing several key advantages:

- **Centralized Logic**: All floor generation logic is consolidated in a single access point
- **State Management**: Eliminates the need for multiple instances while maintaining stateless operations
- **Memory Efficiency**: Avoids unnecessary object creation during game execution
- **Testing Simplification**: Provides a consistent interface for unit testing

```scala
object FloorGenerator:
  def generateFloor(player: Player, difficulty: Int, seed: Long): World =
    // Implementation details
```

## Room Arrangement Implementation

### Matrix-Based Room Distribution

The system uses a functional approach to distribute rooms across a grid structure:

```scala
val numRoomsFloor = (ROOMS.length + NUM_ROWS_DUNGEON - 1) / NUM_ROWS_DUNGEON
val shuffledRooms = Random.shuffle(ROOMS)
val matrixRooms: List[List[String]] = shuffledRooms.grouped(numRoomsFloor).toList
```

**Key Implementation Features:**

1. **Randomization**: Uses `Random.shuffle()` to ensure room placement variety
2. **Grid Organization**: Employs the `grouped()` method for efficient matrix creation
3. **Deterministic Seeding**: Implements `Random.setSeed(seed)` for reproducible results

### Refactoring for Idiomatic Scala

The initial implementation using manual index calculations and mutable variables was refactored
to use functional programming patterns:

**Before (Imperative Approach):**
```scala
var j = 0
shuffledRooms.zipWithIndex.foreach((roomName, i) =>
  if i >= m * (j + 1) then j += 1
  // Complex index calculations...
)
```

**After (Functional Approach):**
```scala
(for
  (row, rowIndex) <- matrixRooms.zipWithIndex
  (roomName, colIndex) <- row.zipWithIndex
yield
  // Clean, immutable operations...
).toMap
```

## Room Positioning and Coordinate System

### Coordinate Calculation

Room positioning uses a helper function to eliminate code redundancy:

```scala
private def calculateStartEnd(index: Int, size: Int): (Int, Int) = 
  val start = index * size + Random.between(MIN_PADDING, MAX_PADDING)
  val end = (index + 1) * size - Random.between(MIN_PADDING, MAX_PADDING)
  (start, end)
```

This approach:
- **Reduces Duplication**: Eliminates repeated coordinate calculation logic
- **Adds Randomization**: Introduces padding variation for visual diversity
- **Maintains Structure**: Ensures rooms fit within their designated grid cells

## Room Connectivity System

### Direction-Based Connection Mapping

The system implements a sophisticated connection mapping using Scala's pattern matching and safe
collection access:

```scala
private def getConnections(
  matrixRooms: List[List[String]],
  row: Int,
  col: Int
): Map[Direction, String] =
  Direction.values.flatMap(
    _ match
      case Direction.West if col > 0 =>
        matrixRooms(row).lift(col - 1).map(Direction.West -> _)
      case Direction.East =>
        matrixRooms(row).lift(col + 1).map(Direction.East -> _)
      case Direction.North if row > 0 =>
        matrixRooms.lift(row - 1).flatMap(_.lift(col)).map(Direction.North -> _)
      case Direction.South =>
        matrixRooms.lift(row + 1).flatMap(_.lift(col)).map(Direction.South -> _)
      case _ => None
  ).toMap
```

### Safe Collection Access with `lift`

The implementation uses Scala's `lift` method for exception-safe collection access:

- **Purpose**: Prevents `IndexOutOfBoundsException` when accessing matrix boundaries
- **Return Type**: Returns `Option[T]` instead of throwing exceptions
- **Functional Composition**: Enables clean chaining with `map` and `flatMap` operations

## Functional Programming Patterns

### For-Comprehension Usage

The implementation leverages for-comprehensions for clean iteration over the room matrix:

```scala
(for
  (row, rowIndex) <- matrixRooms.zipWithIndex
  (roomName, colIndex) <- row.zipWithIndex
yield
  val (startRow, endRow) = calculateStartEnd(colIndex, areaWidth)
  val (startCol, endCol) = calculateStartEnd(rowIndex, areaHeight)
  val connections = getConnections(matrixRooms, rowIndex, colIndex)
  
  roomName -> Room(roomName, Point2D(startRow, startCol), 
                   Point2D(endRow, endCol), connections)
).toMap
```

### Immutability and Pure Functions

All functions in the implementation are pure and side effect free (except for the controlled random seeding):
- **No Mutable State**: All operations use immutable data structures
- **Referential Transparency**: Functions produce the same output for the same input
- **Composability**: Functions can be easily combined and tested in isolation

## Testing Strategy

The implementation supports comprehensive testing through:

1. **Deterministic Generation**: Fixed seed values ensure reproducible test results
2. **Boundary Testing**: `lift` usage enables safe testing of edge cases
3. **Property-Based Testing**: Pure functions allow for property-based test approaches
4. **Unit Testing**: Each function can be tested independently

**Example Test Structure:**
```scala
it should "generate deterministic results with same seed" in: 
  val world1 = FloorGenerator.generateFloor(player, difficulty, seed)
  val world2 = FloorGenerator.generateFloor(player, difficulty, seed)
  world1 shouldEqual world2

```

## Performance Considerations

### Object Creation Optimization

The singleton pattern eliminates unnecessary object instantiation while maintaining functional purity.
The debate between singleton objects and class instantiation was resolved in favor of:

- **Stateless Operations**: No need for multiple instances
- **Memory Efficiency**: Single object handles all generation requests
- **Thread Safety**: Immutable operations ensure concurrent access safety

### Collection Performance

The use of `grouped()` and `lift()` provides optimal performance characteristics:
- **`grouped()`**: O(n) complexity for matrix creation
- **`lift()`**: O(1) complexity for safe access with minimal overhead
- **Lazy Evaluation**: Iterator-based operations where applicable

## Error Handling

The implementation provides robust error handling through:

1. **Option Types**: Safe handling of missing connections and out-of-bounds access
2. **Pattern Matching**: Exhaustive case coverage for all directions
3. **Boundary Checks**: Guard conditions for matrix edge cases
4. **Seed Validation**: Controlled randomization with proper seeding

## Code Quality and Maintainability

The final implementation demonstrates some software engineering best practices:

- **Single Responsibility**: Each function has a clear, focused purpose
- **DRY Principle**: Helper functions eliminate code duplication
- **Readability**: Functional composition creates self-documenting code
- **Type Safety**: Scala's type system prevents runtime errors
- **Scalability**: Modular design allows easy extension and modification
