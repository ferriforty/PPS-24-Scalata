---
title: Entities
layout: default
parent: Implementations
---

# Implementation of Entities

This section describes the implementation choices for the domain entities, 
focusing on how each is constructed using Scala’s traits (mix-ins), interoperability with use cases, 
and immutability patterns.

## 1. Entity Construction and Mix-in Pattern

All core game entities—such as `Player`, `Enemy`, and `Item` subtypes—are implemented using Scala’s case 
classes with functionality injected via trait mix-ins. This enables modular, 
type-safe composition of behavior and strict immutability.

### Mix-in Example

```scala
final case class Enemy(
  override val id: String,
  override val name: String,
  enemyType: EnemyClasses,
  override val position: Point2D,
  override val health: Int,
  override val maxHealth: Int,
  override val attackPower: Int
) extends Entity
  with Alive[Enemy]
  with Movable[Enemy]
  with Combatant[Player]
```
- **Traits** such as `Alive`, `Movable`, and `Combatant` each contribute self-contained functionality, e.g., 
hit point management, spatial movement, and attacking.
- The same pattern is used for `Player`, which also mixes in the `Inventory` trait, and for items, which mix 
in capabilities such as `Pickable`, `Usable`, or `Interactable`.

### Benefits
- **Orthogonal capabilities:** Entities implement only the needed behaviors, keeping the hierarchy flat and 
maintainable.
- **Immutability:** Every state update produces a new value object, ensuring functional purity and 
referential transparency.

Certainly! Here is a revised version of **Section 2. Entity Creation**, adapted to explicitly document the use of **Factories** for creating entities in your project’s implementation report:

## 2. Entity Creation

Entities in the system are **always instantiated via dedicated Factory classes** 
rather than by direct constructor calls. This pattern centralizes and standardizes the creation 
logic for all main entity types, ensuring consistent setup and enforcing invariants 
(such as unique IDs, valid initial states, and correct trait composition).

By routing all entity instantiation through factories, the implementation achieves:
- **Centralised validation and construction rules** 
(e.g., no direct object creation that could bypass business invariants).
- **Easier reconfiguration and testing** (factories can be mocked or parameterized for different scenarios).
- **Scalability** (new entity types or attributes can be integrated by extending only the relevant factory logic).

## 3. Use Case Integration

Use cases encapsulate domain logic (commands like move, attack, use item, interact, etc.) and are separated 
from entities to ensure clarity and a clear architecture.

### How Entities Call or Participate in Use Cases

Entities themselves do **not** invoke use cases directly. Instead:
- **Use cases** are responsible for orchestrating the game logic, accepting entity values as parameters and 
returning updated entities or sessions.
- For example, a `PlayerAttackUseCase` is called by the application layer, taking the current `GameSession` 
and a direction. It queries for the player, fetches the intended enemies, and calls the respective entity's 
methods (`attack`, `takeDamage`, etc.).
- All transformations return new immutable objects (e.g., a new `GameSession` with updated world state).

#### Example (Attack Use Case)
```scala
//
...
currentRoom.enemies.map: e =>
  if e.isAlive && attackLine.contains(e.position) then
    player.attack(e)
  else e
...
// returns a new GameSession with updated entities
```
- The attack logic is implemented in the `advance` method of `PlayerAttackUseCase`. 
The attack is *delegated* to the `Player` and `Enemy` methods, which are implemented in their respective 
case classes using trait methods.

#### Inventory & Item Usage
- The `PlayerInventoryUseCase` allows players to "use" items in their inventory by leveraging the 
`Usable` type class instance for items. Each concrete item (Weapon, Potion, Dust) implements the `Usable` 
trait with specific side effects (e.g., healing, equipping).

#### Player/Enemy Movement & Interactions
- Movement and interaction are driven by use cases such as `PlayerMovementUseCase` and `PlayerInteractUseCase`. 
These operate on snapshotted entities, changing their position or handling item pickup/interaction accordingly.

#### Enemy Phases
- Similarly, AI logic is encapsulated in use cases for enemy attack and movement 
(`EnemyAttackUseCase`, `EnemyMovementUseCase`). These apply Prolog-based AI or simple adjacency checks 
and rely on entity mix-in methods for behavior.

## 4. Item Implementation and Mix-in Application

Concrete items in the domain each extend the abstract `Item` class:
- **Weapon:** Adds attack bonuses, is both `Pickable` and `Usable`.
- **Potion:** Restores health, is `Pickable` and `Usable`.
- **Dust:** Collectible but with no specific effect.
- **Sign and ExitDoor:** Implement `Interactable`, providing in-world effects without being owned in inventory.

Each item provides its `interact` method to encapsulate side effects, return updated sessions, 
or trigger errors (`GameResult.error`).
