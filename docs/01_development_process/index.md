---
title: Development Methodology
layout: default
nav_order: 2
---

# Development Methodology

This section describes the organization of the development work for **Scalata**.

---

## Approach and Workflow

The project follows an **Agile-inspired workflow**, based on short, focused iterations similar to **Scrum sprints**. 
Development is structured into **four sprints**, each lasting approximately 15 hours, 
enabling frequent progress reviews and incremental improvements.

A **Kanban board**—created in an Excel spreadsheet—visually tracks progress. It is used to:

- Prioritize and manage short-term tasks
- Plan upcoming features
- Monitor progress effectively

---

## Sprint Activities

Each sprint includes the following activities:

- **Sprint Planning**  
  At the beginning of each sprint, tasks to be implemented are identified and broken down into smaller, 
more manageable units. These tasks are then estimated in terms of complexity and outlined in detail.

- **Daily Check-Ins**  
  Although not formalized as daily Scrum meetings, short daily reviews ensure that progress remains on track 
and any obstacles are addressed promptly.

- **Sprint Review**  
  At the end of the sprint, the implemented features are evaluated to ensure they align with the project’s goals.

- **Sprint Retrospective**  
  The overall development process is assessed to identify improvements for the next sprint.

---

## Test-Driven Development (TDD)

**Test-Driven Development (TDD)** is applied wherever possible to integrate testing early in the process and to 
minimize maintenance costs and risk.

The **Red-Green-Refactor (RGR)** cycle is followed:

1. **Red**: Write a failing test for the desired functionality.
2. **Green**: Implement code to pass the test.
3. **Refactor**: Clean up and improve the codebase.

**Tools used for TDD**:

- **ScalaTest** for writing unit tests in Scala
- **SCoverage** for measuring test coverage

Given that testing is focused on the model, the **FlatSpec** testing style is used for its balance between 
simplicity and a more user-oriented, declarative approach.

---

## Quality Assurance

To maintain a high-quality codebase, the following tools are utilized:

- **Scala Formatter** for enforcing code style in Scala

---


## Continuous Integration

A **GitHub Actions** workflow runs automated tests across multiple operating systems whenever updates are pushed to the 
dev and master branch. This ensures that the code remains consistent and functional throughout the development process.

---

## Continuous Delivery

Another workflow automates the release process to **GitHub Releases** and create a CHANGELOG every time a new version is 
pushed to the master branch.

---

## Versioning

The project follows **Semantic Versioning**:

- **Major** for incompatible API changes
- **Minor** for backward-compatible feature additions
- **Patch** for backward-compatible bug
