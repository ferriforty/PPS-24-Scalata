---
title: Requirements
layout: default
nav_order: 3
---

# Requirements

## 1  Vision

Deliver an infinitely replayable *text-based roguelike* where every floor of an endless tower is procedurally 
generated and challenge escalates through tougher enemies, scarcer loot and occasional boss encounters. 
Success is measured by:
-  the player reaching higher floors without game-breaking layouts,
-  deterministic seeds that reproduce runs for debugging,
-  extensibility—new content can be added without touching core logic,
-  ≥ 80% automated test coverage.

## 2  Stakeholders

| Role                                  | Interest                                                              |
|---------------------------------------|-----------------------------------------------------------------------|
| **Student developer (Product Owner)** | Completes academic objectives and gains experience.                   |
| **Players (classmates, professors)**  | Enjoy challenging, fair gameplay and evaluate code quality.           |
| **Course examiners**                  | Assess engineering artefacts (SRS, design, tests) against guidelines. |

## 3  Domain Model (informative)

| Entity            | Description                                                        |
|-------------------|--------------------------------------------------------------------|
| **Player**        | User-controlled hero with stats, inventory, visibility radius.     |
| **Enemy**         | AI-driven creature with health, attack power and behaviour script. |
| **NPC**           | Non-hostile trader that barters items (future).                    |
| **Item**          | Lootable object (weapon, potion, special key, etc.).               |
| **Room**          | Rectangular area inside a floor, bounded by walls and doors.       |
| **Floor / Level** | Set of rooms linked by doors; contains staircase to next level.    |
| **World**         | Aggregate of floors visited in the current run.                    |
| **GameSession**   | Immutable snapshot: world + game state history (for undo).         |

## 4  Functional Requirements (FR)

Each requirement is testable and prefixed **FR-x.y** for traceability.

### 4.1 Player Interaction
- **FR-P.01** The player shall move one grid cell per turn in the four cardinal directions using 
both *wasd* keys and full words (“north”, “south”, …).
- **FR-P.02** The player shall attempt a melee attack in a chosen direction; damage = base + weapon bonus.
- **FR-P.03** The player shall pick up an item occupying the same cell, adding it to inventory.
- **FR-P.04** The player shall drop, use or equip inventory items by textual commands 
(“use potion”, “equip halberd”).
- **FR-P.05** The player shall trade items with an NPC when both occupy adjacent cells.
- **FR-P.06** The player shall ascend when standing on a staircase, starting a new floor 
with incremented difficulty.
- **FR-P.07** The player shall request contextual *help* that lists all available commands.
- **FR-P.08** The player shall undo the latest successful turn back to the run’s root state, 
unless already at root (error raised).

### 4.2 World Generation & Progression
- **FR-W.01** For each new floor, the system shall generate 4–8 rooms laid out in a 2 × N matrix, 
guaranteeing full connectivity.
- **FR-W.02** The generator shall ensure exactly one staircase in the last room and no unreachable areas.
- **FR-W.03** Enemy count per room shall follow a Gaussian distribution centred on *difficulty* with 
bounds 1–3 (see code constant `gaussianBetween`).
- **FR-W.04** Loot rarity shall inversely correlate with difficulty: probability 
(potion) ≥ 0.8 on level 1 and ≤ 0.2 by level 10.
- **FR-W.05** When a numeric seed is supplied, two executions with the same seed and difficulty 
shall produce identical floor layouts and enemy placements.

### 4.3 Enemy
- **FR-E.01** Each living enemy shall decide one action per turn (move or attack) after the player acts 
(turn-based loop).
- **FR-E.02** Enemies shall use breadth-first search over the room grid to select the shortest walkable 
path toward the player if within visibility radius.
- **FR-E.03** If adjacent to the player, an enemy shall perform a melee attack and remain in place.

### 4.4 Game State Management
- **FR-S.01** The system shall maintain an immutable history of `(World, GameState)` snapshots for 
every successful turn.
- **FR-S.02** An *undo* command shall roll back to the previous snapshot; a second consecutive 
*undo* rolls back again, etc.
- **FR-S.03** On fatal player death, the system shall display “Game Over” and prevent further input 
until a new game is started.

### 4.5 User Interface & Feedback
- **FR-UI.01** The terminal view shall render the current room with ASCII symbols.
- **FR-UI.02** Status bar shall display current health, equipped weapon, inventory count, 
floor number and last event note.
- **FR-UI.03** Error messages (e.g., invalid input, blocked path) shall appear within one line prefixed “! ”.
- **FR-UI.04** The *clear* command shall redraw the entire viewport without altering game state.

### 4.6 Error Handling
- **FR-EH.01** On command parsing failure, the system shall return `GameResult.Error(InvalidInput)` 
without changing world state.
- **FR-EH.02** Attempting to use an item not owned shall raise `ItemNotOwned`, preserving state.
- **FR-EH.03** Requests to move into walls, closed doors or occupied cells shall raise `InvalidDirection`.

## 5  Non-Functional Requirements (NFR)

| ID            | Category    | Requirement                                                                                                                  |
|---------------|-------------|------------------------------------------------------------------------------------------------------------------------------|
| **NFR-US.01** | Usability   | *Help* command must list all valid verbs and examples.                                                                       |
| **NFR-US.02** | Usability   | Novice players must complete tutorial floor in ≤ 5 min (observational study).                                                |
| **NFR-RE.01** | Reliability | Game shall not crash on malformed input; unhandled exceptions count ≤ 1 per 10 000 commands during test suite.               |
| **NFR-PE.01** | Performance | Floor generation time  Procedural generation, permadeath and turn-based loops are the canonical pillars of roguelike design. |
