---
title: Design Architecture
layout: default
nav_order: 4
---

# Design Architecture

The architectural design of **Scalata** is organized into two main layers: the **Engine** and the **Application**. 
The architecture follows **Domain-Driven Design (DDD)** principles and uses the **Model-View-Controller (MVC)** pattern  
to ensure a clean separation of responsibilities. 
It also incorporates design patterns like the **Builder Pattern** for constructing complex world structures.

---

## 📐 General Architecture Overview

GRAPH TO-DO


---

## ⚙️ Engine

The Engine is the core of the system and is implemented using **Domain-Driven Design (DDD)** principles. 
It is responsible for handling the game world logic and simulation, independent of any graphical interface.

### 📦 Domain Model

This layer contains:

- **Entities**: World elements such as terrains, objects, and characters.
- **Value Objects**: Data structures with no identity (e.g. coordinates, colors).
- **Aggregates**: Logical groupings of entities with a root for consistency.
- **Repositories**: Interfaces for accessing aggregates (not implemented directly in engine).

### 🧠 Application Services

These represent use cases that orchestrate the domain logic. They provide:

- World creation and initialization
- Game loop progression
- Event handling
- Player actions processing

### 📢 Domain Events

Key events are emitted during the simulation to signal changes:

- `WorldUpdatedEvent`
- `LevelCompletedEvent`
- `EntityMovedEvent`
- `AchievementUnlockedEvent`

These can be subscribed to by the Application layer for reactive UI updates.

---

## 🏗️ Builder Pattern

The **Builder Pattern** is used to construct complex `World` instances by composing various optional elements 
(NPCs, terrain, collectibles).

### ✅ Benefits:
- Step-by-step construction
- Separation of construction from representation
- Reusability of builder logic for different world configurations


