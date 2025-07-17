---
title: Requirements
layout: default
nav_order: 3
---

# Requirements

## 1  Vision

Deliver an infinitely replayable *text-based roguelike* where every floor of an endless tower is procedurally 
generated and challenge escalates through tougher enemies and better loot. 
Success is measured by:
-  the player reaching higher floors without game-breaking layouts,
-  deterministic seeds that reproduce runs for debugging,
-  extensibility—new content can be added without touching core logic,
-  ≥ 70% automated test coverage.

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
| **Item**          | Pickable object (weapon, potion, special key, etc.).               |
| **Room**          | Rectangular area inside a floor, bounded by walls and doors.       |
| **Floor / Level** | Set of rooms linked by doors; contains staircase to next level.    |
| **World**         | Aggregate of floors visited in the current run.                    |
| **GameSession**   | Immutable snapshot: world + game state history (for undo).         |

## 4  Functional Requirements (FR)

Each requirement is testable and prefixed **FR-x.y** for traceability.

### 4.1 Player Interaction
- **FR-P.01** The player shall move one grid cell per turn in the four cardinal directions using 
both *wasd* keys.
- **FR-P.02** The player shall attempt a melee attack in a chosen direction; damage = base + weapon bonus.
- **FR-P.03** The player shall pick up an item in a close cell interacting with it, adding it to inventory.
- **FR-P.04** The player use inventory items by textual commands 
(“use potion”).
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
bounds 1–MAX_ENEMY.
- **FR-W.04** Loot rarity shall correlate with difficulty: probability 
(small potion) ≥ 0.8 on level 1 and ≤ 0.2 by level 10.
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

## 5. Non-Functional Requirements (NFR)

| ID            | Category        | Requirement                                                                                                                          |
|---------------|-----------------|--------------------------------------------------------------------------------------------------------------------------------------|
| **NFR-US.01** | Usability       | The game must support a `help` command that prints all available player actions, grouped by category, with examples of usage.        |
| **NFR-US.02** | Usability       | A novice player must be able to complete the tutorial floor (Level 1) in under **5 minutes** on 90% of test runs.                    |
| **NFR-RE.01** | Reliability     | The game must handle invalid inputs (e.g., wrong command, syntax error) without crashing or altering game state.                     |
| **NFR-RE.02** | Reliability     | Uncaught runtime exceptions must not exceed **1 per 10,000** executed commands during integration tests.                             |
| **NFR-PE.01** | Performance     | Procedural generation of a new floor must complete in under **100 milliseconds** on a 2.0 GHz CPU with ≤ 512 MB RAM.                 |
| **NFR-PE.02** | Performance     | Resolution of a full game turn (player + up to 10 enemy AIs) must complete in under **10 milliseconds** per turn.                    |
| **NFR-TE.01** | Testability     | The codebase must achieve at least **80% line coverage** and **60% branch coverage** as measured by Scoverage.                       |
| **NFR-MA.01** | Maintainability | All public APIs must be documented using Scaladoc; the code must be formatted with Scalafmt and contain no TODOs in committed files. |
| **NFR-EX.01** | Extensibility   | New enemy or item types must be addable exclusively by extending factory methods, without altering any existing Domain class.        |
| **NFR-PO.01** | Portability     | The application must be executable on Windows, macOS, and Linux using **OpenJDK 17+**, with no OS-specific dependencies.             |
| **NFR-SC.01** | Scalability     | The game engine must support generating levels with **up to 1000 enemies** without exceeding 512 MB RAM or 2 seconds of turn time.   |

## 6. Technology Stack

Below is the comprehensive list of technology choices and tooling adopted for the project, 
covering both main dependencies and development workflow:

- **Scala 3.3.6**
- **SBT 1.10.1**
- **SBT Assembly 2.3.0**  
- **Scalatest 3.2.19**
- **Cats Effect 3.6.2**  
  Library for functional effect handling, enabling pure functional programming and resource safety.
- **JLine 3.30.4**  
  Dependable Java library for advanced console and line-oriented user input, supporting both classic CLI and 
extended terminal experiences.
- **tuProlog 3.3.0**  
  Logic programming engine for Prolog-based AI, enemy decision-making, and map logic.
- **Scoverage 2.2.2**  
  For code coverage analysis, enforcing and tracking minimum coverage requirements project-wide.
- **Scalafmt 2.5.2**  
  Automated code formatter ensuring consistent code style across the team.
- **GitHub Actions**  
  Used to enable continuous integration, run test suites on every push, and automate releases.
- **Immutability-first design**  
  The entire application favors value objects and immutable data structures; 
no mutable state is permitted in domain or use case code.
- **Semantic Release**  
  Automated project versioning and changelog generation to ensure clear release history.
- **Scaladoc**  
  All public APIs are documented using Scala’s built-in documentation tooling to ease maintainability 
and onboarding.

These technology choices were made to fulfill both the functional and non-functional requirements of the project, 
such as performance, reliability, scalability, testability, and maintainability. The toolchain also enables high 
code quality, continuous feedback, and future extensibility with minimal technical debt.
