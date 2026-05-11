# CS Lab: Dungeons &amp; Dragons Adventure Simulator

In this lab you will build a tiny turn-based combat simulator inspired by
tabletop role-playing games. You will complete **seven methods** in a single
Java file. When all methods are correct, your program will produce two text
files: an adventure log and an ASCII map of the dungeon, round by round.

## Project structure

```
DungeonLab/
├── lib/
│   └── junit-platform-console-standalone.jar   (do not modify)
├── src/
│   ├── main/java/dungeon/
│   │   └── DungeonAdventure.java                ← YOUR WORK GOES HERE
│   └── test/java/dungeon/
│       └── DungeonAdventureTest.java            (do not modify)
└── README.md
```

## Step 1 — Compile

Open a terminal in the `DungeonLab/` folder and run:

**Mac / Linux:**
```
javac -cp lib/junit-platform-console-standalone.jar \
      -d out \
      src/main/java/dungeon/DungeonAdventure.java \
      src/test/java/dungeon/DungeonAdventureTest.java
```

**Windows (Command Prompt):**
```
javac -cp lib\junit-platform-console-standalone.jar -d out src\main\java\dungeon\DungeonAdventure.java src\test\java\dungeon\DungeonAdventureTest.java
```

## Step 2 — Run the tests

After completing each method, run the tests to check your work:

**Mac / Linux:**
```
java -jar lib/junit-platform-console-standalone.jar \
     --class-path out \
     --select-class dungeon.DungeonAdventureTest
```

**Windows:**
```
java -jar lib\junit-platform-console-standalone.jar --class-path out --select-class dungeon.DungeonAdventureTest
```

Aim for all 47 tests to pass before submitting.

## Step 3 — Run the simulation

Once all tests pass, run the full simulation:

```
java -cp out dungeon.DungeonAdventure
```

This creates two files in the project folder:
- **adventure.txt** — the round-by-round log of the battle
- **dungeon.txt** — the ASCII map of the dungeon for each round

You can pass an optional seed argument to get a different adventure:
```
java -cp out dungeon.DungeonAdventure 1234
```

## What the methods do

| Part | Method | Purpose |
|------|--------|---------|
| 1 | `rollDice` | Foundation: roll N dice, return the sum |
| 2 | `computeAttackDamage` | Combat math: weapon roll + strength − armor |
| 3 | `distanceBetween` | Chebyshev distance on the dungeon grid |
| 4 | `canReach` | Whether a character can reach a target this turn |
| 5 | `moveToward` | One step toward a target square |
| 6 | `describeAttack` | Build the log line for one attack |
| 7 | `generateEncounter` | Pick a random monster, build the intro line |

Read the comment block above each method for full details, including
worked numerical examples and hints.
