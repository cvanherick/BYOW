# Build Your Own World

CS61B Project 3: a tile-based Java game engine with seeded procedural world generation, player movement, enemies, line-of-sight rendering, theme switching, multilingual menus, and save/load support.

## Recruiter Overview

This project demonstrates object-oriented systems design in Java. The game turns a numeric seed into a deterministic world, renders that world with a tile engine, accepts keyboard input, updates game state, and persists/restores sessions.

The original Java implementation can be shared directly. The portfolio case study points here as the source of truth for the actual playable code.

## Features

- Seeded procedural generation of rooms and connecting hallways
- Tile-based rendering with the CS61B tile engine
- Avatar movement and collision behavior
- Enemy pathfinding toward the player
- Optional line-of-sight rendering mode
- Theme switching across multiple tile palettes
- English/French menu mode
- Save/load support through `:Q` and `L`
- Autograder-style input simulation through `AutograderBuddy`

## Project Structure

```text
src/core/
  Main.java             Entry point
  Menu.java             Menu, seed entry, rendering loop, controls
  World.java            Seeded world generation and global game state
  Movements.java        Avatar movement and enemy turn cadence
  Enemy.java            Enemy placement and pathfinding
  LineOfSight.java      Limited-visibility renderer
  SaveLoadGame.java     File-backed persistence
  AutograderBuddy.java  Deterministic input simulation helper

src/tileengine/         Rendering and tile definitions
src/utils/              File and random utility helpers
tests/                  JUnit test scaffold
```

## Run

This project expects the CS61B `library-fa24` dependency folder at `../../library-fa24`, matching the original course workspace layout.

```bash
make compile
make run
```

To build the recruiter demo JAR from the actual Java source:

```bash
make jar
java -jar dist/byow-demo.jar
```

Controls:

- `N`: new game
- Enter a numeric seed, then `S`
- `W`, `A`, `S`, `D`: move
- `N`: toggle line of sight while in game
- `T`: switch tile theme
- `:Q`: save and quit
- `L`: load saved game from the menu
- `F` / `E`: switch menu language

## Verification

```bash
make compile
make compile-tests
```

`AutograderBuddy.getWorldFromInput(...)` supports deterministic input strings such as:

```text
n12345swwa:q
lssdd
```

This makes it possible to compare world states without opening the interactive renderer.

## Notes

- Save data is written to `savedGamed.txt`, matching the existing project behavior.
- Generated files should stay out of version control via `.gitignore`.
