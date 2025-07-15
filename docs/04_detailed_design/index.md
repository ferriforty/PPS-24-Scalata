---
title: Detailed Design
layout: default
nav_order: 5
---

## Dynamic View – Typical Turn Sequence

![plot](../04_detailed_design/sequence_arch_sclata.png)

*Single-threaded execution* guarantees deterministic order; hot paths (floor generation, AI) stick to
immutable data to meet performance constraints.