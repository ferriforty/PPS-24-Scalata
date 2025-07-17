---
title: Retrospective
layout: default
nav_order: 7
---

# Retrospective

## Initial Objectives

The main objective of the project was to deliver a highly replayable, robust text-based roguelike game, 
ensuring software quality through clean architectural design, broad extensibility, and a solid testing base. 
The project was also designed as a challenge and practice ground in applying functional and 
modular programming principles.

**Key goals:**
- Guarantee scalability and maintainability by separating domain, application, and infrastructure layers.
- Adopt immutable data structures and effectful programming only at system boundaries.
- Maximize automated test coverage and continuous quality assurance.
- Allow for future content and interface expansion with minimal code ripple.

## Development Practices

### Methodology

An Agile-inspired approach, organized into four short sprints with Kanban boards, governed development. 
Tasks were incrementally prioritized and tracked, supporting rapid iteration and regular review of progress 
and priorities.

- **Sprint Planning** focused on identifying and sizing work items.
- **Daily Check-ins** enabled steady progress, adapting quickly to any issues.
- **Retrospectives** at the end of each sprint allowed continuous improvement in practices and planning.

### Testing and Quality

Test-Driven Development (TDD) was a core part of the workflow:

- ScalaTest and SCoverage were used for automated tests and coverage metrics.
- The FlatSpec style was chosen to promote readability and user-oriented, declarative test specifications.
- Code formatting was enforced automatically with Scalafmt.
- Continuous Integration (CI) was set up on GitHub Actions to run the full test suite and publish releases for every update.
- Tests were developed not only for functional requirements but also to verify non-functional requirements.
    - **Performance tests** ensured floor generation and game turns met required speed constraints.
    - **Reliability and robustness** were validated through tests for error handling, state undo, 
  and invalid input scenarios.
    - **Testability** requirements (such as minimum coverage) were themselves enforced through coverage 
  thresholds and branch coverage inspection in CI.
    - **Extensibility** was assured via tests that adding new item or enemy types would require changes only 
  in factory logic, not in domain classes.

This comprehensive approach guaranteed high software quality both in core business logic and in system-level 
properties, fully supporting the project's non-functional requirements.

### Versioning

Semantic Versioning guidelines were followed to provide clear traceability for features and bug fixes.

## Achievements

- **Modular, maintainable, and testable codebase** with enforced separation of domain, use-cases, 
and infrastructure.
- **Immutability and type safety** in all business objects, reducing bugs and enabling safe refactoring.
- **Reproducible world generation** by using seeded randomness.
- **Full adaptability**: adding new UI (GUI, web), new item or enemy types, or new features can be 
done with local changes only.
- **Scalable design** supporting up to 1000 enemies per floor with efficient performance.

## Lessons Learned & Challenges

- **Entity complexity required careful documentation**: The flexibility from mix-in traits needed rigorous 
specification and testing to ensure predictable behavior.
- **Testing UI-independent business logic**: The separation of core and infrastructure enabled deep testing, 
though mapping and DTO code were needed for bridging.
- **Managing side effect boundaries**: Effect-polymorphism (with monad stacks) maximized testability, 
although it introduced an initial learning curve.
- **Ensuring architectural discipline**: Strong enforcement of inward-facing dependencies allowed safe, 
rapid development, while anticipating future growth and ease of maintenance.

## Future Work

- **Parameterizable Factories**: Making factories data-driven (e.g., external JSON/YAML) to support dynamic 
content pipelines.
- **UI Expansion**: Adding graphical or web user interfaces with no change to the core logic.
- **Augmented Automation**: Stronger property-based and integration testing for complex interactions.
- **Engine Optimization**: Profiling and refactoring core routines for even larger game scenarios.

## Personal Reflection

This project was both a test and an opportunity to integrate advanced software design taught during the 
Master’s degree. Principles such as functional purity, responsibility-driven composition, and strong 
separation of concerns paid off in maintainability, reliability, and extensibility. The process reinforced the 
value of clean architecture, documentation, and disciplined workflows, preparing for future work in 
challenging engineering contexts.
