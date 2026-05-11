package dungeon;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * CS Lab: Dungeons &amp; Dragons Adventure Simulator
 *
 * In this lab you will build a tiny turn-based combat simulator inspired by
 * tabletop role-playing games. A small party of adventurers explores a 20x10
 * grid dungeon, encounters a monster, and fights it out turn by turn. Your
 * program will produce a text "adventure log" and an ASCII map of the dungeon
 * for every round.
 *
 * You will complete SEVEN methods, each marked with TODO. Read each method's
 * comment carefully: it tells you exactly what the method receives, what it
 * must calculate, and what it must return.
 *
 * DO NOT change any method signatures (names, parameter types, return types).
 * DO NOT change the main() method, runSimulation(), or any helper that is not
 * marked with a TODO. These call your methods to drive the simulation.
 *
 * How to compile and run from the project root:
 *   javac -d out src/main/java/dungeon/DungeonAdventure.java
 *   java  -cp out dungeon.DungeonAdventure
 *
 * If everything is correct, two files will appear in the project folder:
 *   - adventure.txt  : the full text log of what happened
 *   - dungeon.txt    : the ASCII map for each round
 *
 * Open them in any text editor to read your adventure!
 *
 * @author [Your name here]
 */
public class DungeonAdventure {

    // -------------------------------------------------------------------------
    // Constants — do not change these values
    // -------------------------------------------------------------------------

    /** Width of the dungeon grid (number of columns). */
    public static final int DUNGEON_WIDTH = 20;

    /** Height of the dungeon grid (number of rows). */
    public static final int DUNGEON_HEIGHT = 10;

    /** Maximum number of combat rounds before the simulation stops. */
    public static final int MAX_ROUNDS = 20;

    // -------------------------------------------------------------------------
    // Character "type" constants — used as int codes in the type[] array.
    // Think of these like channel numbers: WARRIOR is 0, WIZARD is 1, etc.
    // -------------------------------------------------------------------------

    public static final int WARRIOR = 0;   // Strong melee fighter, low magic
    public static final int WIZARD  = 1;   // Powerful spells, low health
    public static final int ROGUE   = 2;   // Fast and sneaky, balanced stats
    public static final int CLERIC  = 3;   // Healer, medium fighter
    public static final int MONSTER = 4;   // The enemy

    /** Human-readable names for each type code, indexed by the constants above. */
    public static final String[] TYPE_NAMES = { "Warrior", "Wizard", "Rogue", "Cleric", "Monster" };

    /** Single-letter symbols for the ASCII map, indexed by the type codes. */
    public static final char[] TYPE_SYMBOLS = { 'W', 'Z', 'R', 'C', 'M' };

    // -------------------------------------------------------------------------
    // Movement speeds for each character type, in grid squares per turn.
    // Indexed by the type constants above (so SPEEDS[WARRIOR] = 3, etc.).
    // -------------------------------------------------------------------------
    public static final int[] SPEEDS = { 3, 2, 5, 3, 4 };

    // -------------------------------------------------------------------------
    // Weapon damage dice for each type. The number of sides on the die that
    // is rolled when this character attacks. Indexed by type constants.
    // -------------------------------------------------------------------------
    public static final int[] WEAPON_DICE = { 8, 6, 6, 6, 10 };  // Warrior: d8, etc.

    // =========================================================================
    //                     PART 1 — rollDice()
    // =========================================================================
    /**
     * Roll {@code numDice} fair dice, each with {@code numSides} sides, and
     * return the SUM of all the rolls.
     *
     * <p><b>Background.</b> In tabletop games, dice rolls are written like
     * "2d6" which means "roll two six-sided dice and add them up". This method
     * is the heart of all randomness in the lab — every other random thing
     * (attack damage, encounter type) is built on top of it.
     *
     * <p><b>Worked example.</b> If {@code numDice = 3} and {@code numSides = 4},
     * you should roll three 4-sided dice. Suppose the rolls are 2, 4, 1.
     * The method returns 2 + 4 + 1 = 7.
     *
     * <p><b>How to roll one die.</b> Use the {@code rng} parameter, which is a
     * {@link java.util.Random} object. The expression
     * <pre>{@code   rng.nextInt(numSides) + 1   }</pre>
     * gives you a random integer from 1 to {@code numSides} inclusive.
     * (We add 1 because {@code nextInt(n)} returns 0..n-1 but dice start at 1.)
     *
     * <p><b>Why pass in {@code rng}?</b> So that your tests can use a fixed
     * seed and get the same results every time. The main() method passes you
     * a real Random; the tests pass a seeded one. You don't need to create
     * your own Random inside this method.
     *
     * @param numDice    the number of dice to roll (always &ge; 1)
     * @param numSides   the number of sides on each die (always &ge; 2)
     * @param rng        the random number source — call rng.nextInt(numSides)
     * @return the SUM of all dice rolls
     */
    public static int rollDice(int numDice, int numSides, Random rng) {
        // TODO Part 1: Loop numDice times. On each iteration, roll a die
        //              by computing  rng.nextInt(numSides) + 1  and add it
        //              to a running total. Return the total.
        int pip = 0;
        for (int toss = 0; toss < numDice; toss++) {
            pip += rng.nextInt(numSides) + 1;
        }

        return pip; // <-- replace this
    }

    // =========================================================================
    //                     PART 2 — computeAttackDamage()
    // =========================================================================
    /**
     * Compute the damage dealt by one attack, after armor is taken into account.
     *
     * <p><b>The formula.</b>
     * <ol>
     *   <li>Roll the attacker's weapon: 1 die with {@code weaponDie} sides.
     *       Use your {@link #rollDice(int, int, Random)} method!</li>
     *   <li>Add the attacker's strength bonus: {@code strength}.</li>
     *   <li>Subtract the defender's armor: {@code armor}.</li>
     *   <li>If the result is less than 0, return 0 (an attack never heals
     *       the defender — the worst it can do is bounce off).</li>
     *   <li>Otherwise return the result.</li>
     * </ol>
     *
     * <p><b>Worked example.</b> A Warrior with strength 4 attacks a goblin with
     * armor 2 using a d8 weapon. Suppose the d8 rolls a 5.
     * <br>damage = 5 + 4 - 2 = 7  →  return 7.
     *
     * <p><b>Worked example 2.</b> A Wizard with strength 0 attacks a heavily
     * armored troll (armor 10) using a d6 weapon. Suppose the d6 rolls a 3.
     * <br>raw = 3 + 0 - 10 = -7  →  return 0 (clamped to zero).
     *
     * @param strength    the attacker's strength bonus, added to the roll
     * @param weaponDie   the number of sides on the attacker's weapon die
     * @param armor       the defender's armor value, subtracted from the roll
     * @param rng         the random number source — pass it through to rollDice
     * @return the damage dealt, never less than 0
     */
    public static int computeAttackDamage(int strength, int weaponDie, int armor, Random rng) {
        // TODO Part 2: Call rollDice(1, weaponDie, rng) to get the weapon roll.
        //              Add strength, subtract armor, clamp to 0.
        int faceValue = rollDice(1, weaponDie, rng);
        int rawHit = faceValue + strength - armor;

        return Math.max(0, rawHit); // <-- replace this
    }

    // =========================================================================
    //                     PART 3 — distanceBetween()
    // =========================================================================
    /**
     * Compute the distance between two squares on the dungeon grid, using the
     * "Chebyshev" or "king's-move" distance.
     *
     * <p><b>What is Chebyshev distance?</b> In D&amp;D 5e, a creature can move
     * diagonally and it counts as 1 square (not √2). So the distance between
     * two squares is simply the LARGER of the horizontal and vertical gaps:
     * <pre>{@code   distance = max( |x1 - x2|, |y1 - y2| )   }</pre>
     *
     * <p><b>Worked example 1.</b> From (3, 5) to (3, 5):
     * <br>|3-3| = 0, |5-5| = 0, max = 0  →  return 0.
     *
     * <p><b>Worked example 2.</b> From (1, 1) to (4, 1):
     * <br>|1-4| = 3, |1-1| = 0, max = 3  →  return 3.
     *
     * <p><b>Worked example 3.</b> From (0, 0) to (3, 4):
     * <br>|0-3| = 3, |0-4| = 4, max = 4  →  return 4.
     *
     * <p><b>Hints.</b> {@code Math.abs(int)} gives the absolute value of an
     * int. {@code Math.max(int, int)} gives the larger of two ints.
     *
     * @param x1 column of the first square
     * @param y1 row of the first square
     * @param x2 column of the second square
     * @param y2 row of the second square
     * @return the Chebyshev distance between the two squares
     */
    public static int distanceBetween(int x1, int y1, int x2, int y2) {
        // TODO Part 3: Use Math.abs and Math.max to compute the king's-move
        //              distance between (x1,y1) and (x2,y2).
        int colGap = Math.abs(x1 - x2);
        int rowGap = Math.abs(y1 - y2);

        return Math.max(colGap, rowGap); // <-- replace this
    }

    // =========================================================================
    //                     PART 4 — canReach()
    // =========================================================================
    /**
     * Decide whether a character at {@code (cx, cy)} with movement speed
     * {@code speed} can reach the target square {@code (tx, ty)} in a single
     * turn.
     *
     * <p><b>Rule.</b> A character can reach the target if and only if the
     * Chebyshev distance to the target is less than or equal to {@code speed}.
     *
     * <p><b>Worked example 1.</b> A Rogue at (2, 3) with speed 5 wants to
     * reach (5, 6). Distance = max(|2-5|, |3-6|) = max(3, 3) = 3.
     * 3 ≤ 5 so return true.
     *
     * <p><b>Worked example 2.</b> A Wizard at (0, 0) with speed 2 wants to
     * reach (5, 1). Distance = max(5, 1) = 5. 5 ≤ 2 is false, so return false.
     *
     * <p><b>Hint.</b> Use your {@link #distanceBetween(int, int, int, int)}
     * method! Don't recompute the distance from scratch.
     *
     * @param cx     character's current column
     * @param cy     character's current row
     * @param tx     target column
     * @param ty     target row
     * @param speed  the character's movement speed in squares per turn
     * @return true if the character can reach the target this turn
     */
    public static boolean canReach(int cx, int cy, int tx, int ty, int speed) {
        // TODO Part 4: Use distanceBetween(...) and compare with speed.
        int stepsNeeded = distanceBetween(cx, cy, tx, ty);

        return stepsNeeded <= speed; // <-- replace this
    }

    // =========================================================================
    //                     PART 5 — moveToward()
    // =========================================================================
    /**
     * Move a character ONE STEP toward a target square (closing the distance
     * by 1 in each axis where there is still a gap). Return the new position
     * as a length-2 int array {@code {newX, newY}}.
     *
     * <p><b>Rule.</b> For each of the two axes (x and y) independently:
     * <ul>
     *   <li>If {@code currentX < targetX}, the new x is {@code currentX + 1}.</li>
     *   <li>If {@code currentX > targetX}, the new x is {@code currentX - 1}.</li>
     *   <li>If {@code currentX == targetX}, the new x stays {@code currentX}.</li>
     * </ul>
     * Apply the same rule to the y axis.
     *
     * <p><b>Worked example 1.</b> Move from (2, 5) toward (8, 5):
     * <br>x: 2 &lt; 8 so new x = 3.  y: 5 == 5 so new y = 5.
     * <br>Return {@code new int[]{3, 5}}.
     *
     * <p><b>Worked example 2.</b> Move from (4, 4) toward (1, 7):
     * <br>x: 4 &gt; 1 so new x = 3.  y: 4 &lt; 7 so new y = 5.
     * <br>Return {@code new int[]{3, 5}}.
     *
     * <p><b>Worked example 3.</b> Move from (5, 5) toward (5, 5):
     * <br>Already there. Return {@code new int[]{5, 5}}.
     *
     * <p><b>Hint.</b> Two if/else if/else blocks — one for each axis. Then
     * return {@code new int[]{newX, newY}}.
     *
     * @param currentX  the character's current column
     * @param currentY  the character's current row
     * @param targetX   the target column
     * @param targetY   the target row
     * @return a length-2 int array {newX, newY} — the position after one step
     */
    public static int[] moveToward(int currentX, int currentY, int targetX, int targetY) {
        // TODO Part 5: Compute newX and newY using the rule above.
        //              Return  new int[]{ newX, newY };
        int nudgeX = currentX;
        int nudgeY = currentY;

        if (currentX < targetX) nudgeX = currentX + 1;
        else if (currentX > targetX) nudgeX = currentX - 1;

        if (currentY < targetY) nudgeY = currentY + 1;
        else if (currentY > targetY) nudgeY = currentY - 1;

        return new int[]{ nudgeX, nudgeY }; // <-- replace this
    }

    // =========================================================================
    //                     PART 6 — describeAttack()
    // =========================================================================
    /**
     * Build a single-line string describing one attack, suitable for the
     * adventure log.
     *
     * <p><b>The format is exactly:</b>
     * <pre>{@code   <AttackerName> the <AttackerType> attacks <DefenderName> for <damage> damage!   }</pre>
     * If the damage is 0, use this format instead:
     * <pre>{@code   <AttackerName> the <AttackerType> attacks <DefenderName> but the attack bounces off!   }</pre>
     *
     * <p><b>Worked example 1.</b>
     * <br>attackerName = "Aragorn", attackerType = WARRIOR, defenderName = "Goblin", damage = 7
     * <br>Returns: "Aragorn the Warrior attacks Goblin for 7 damage!"
     *
     * <p><b>Worked example 2.</b>
     * <br>attackerName = "Merlin", attackerType = WIZARD, defenderName = "Troll", damage = 0
     * <br>Returns: "Merlin the Wizard attacks Troll but the attack bounces off!"
     *
     * <p><b>Hints.</b>
     * <ul>
     *   <li>Use {@link #TYPE_NAMES}[attackerType] to get the type's display name.</li>
     *   <li>String concatenation in Java uses the {@code +} operator. You can
     *       chain it: {@code "a" + " " + 5 + " b"} → {@code "a 5 b"}.</li>
     *   <li>Use an {@code if/else} to choose between the two formats.</li>
     *   <li>Match the spacing and punctuation EXACTLY — the tests are picky.</li>
     * </ul>
     *
     * @param attackerName the attacker's name (e.g. "Aragorn")
     * @param attackerType the attacker's type code (one of WARRIOR, WIZARD, ...)
     * @param defenderName the defender's name (e.g. "Goblin")
     * @param damage       the damage dealt (from computeAttackDamage)
     * @return the formatted log line
     */
    public static String describeAttack(String attackerName, int attackerType,
                                         String defenderName, int damage) {
        // TODO Part 6: Build the log string. Use TYPE_NAMES[attackerType] for
        //              the attacker's class. Choose format based on damage == 0.
        String className = TYPE_NAMES[attackerType];
        if (damage == 0) {
            return attackerName + " the " + className + " attacks " + defenderName + " but the attack bounces off!";

        } else {
            return attackerName + " the " + className + " attacks " + defenderName + " for " + damage + " damage!";
        }

    }

    // =========================================================================
    //                     PART 7 — generateEncounter()
    // =========================================================================
    /**
     * Generate a short narrative describing the monster the party encounters,
     * picked randomly based on the party's level.
     *
     * <p><b>Rules.</b>
     * <ol>
     *   <li>Roll a single d4 (1 die with 4 sides) using your {@link #rollDice}
     *       method. The roll is 1, 2, 3, or 4.</li>
     *   <li>Pick the encounter from the table below using the roll:
     *       <table border="1" summary="encounters">
     *         <tr><th>Roll</th><th>Monster</th></tr>
     *         <tr><td>1</td><td>"Goblin Scout"</td></tr>
     *         <tr><td>2</td><td>"Skeleton Warrior"</td></tr>
     *         <tr><td>3</td><td>"Dire Wolf"</td></tr>
     *         <tr><td>4</td><td>"Orc Berserker"</td></tr>
     *       </table>
     *   </li>
     *   <li>Build the result string in this exact format:
     *       <pre>{@code A level <partyLevel> party encounters a <Monster>!}</pre>
     *   </li>
     * </ol>
     *
     * <p><b>Worked example.</b> If {@code partyLevel = 3} and the d4 rolls a 2,
     * return: {@code "A level 3 party encounters a Skeleton Warrior!"}
     *
     * <p><b>Hints.</b>
     * <ul>
     *   <li>Use a {@code switch} statement or {@code if/else if} chain on the
     *       roll, OR put the four names in a {@code String[]} array and index
     *       into it (remember to subtract 1 because rolls are 1-based but
     *       arrays are 0-based!).</li>
     *   <li>Match the wording and punctuation exactly. The exclamation mark
     *       at the end is required.</li>
     * </ul>
     *
     * @param partyLevel  the party's level (any positive int)
     * @param rng         the random number source — pass it through to rollDice
     * @return a one-line narrative string
     */
    public static String generateEncounter(int partyLevel, Random rng) {
        // TODO Part 7: Roll a d4, look up the monster name, build the string.
        int dieRoll= rollDice(1, 4, rng);
        String[] beastiary = {"Goblin Scout", "Skeleton Warrior", "Dire Wolf", "Orc Berserker" };
        String creatureName = beastiary[dieRoll - 1];
        return "A level " + partyLevel + " party encounters a " + creatureName + "!";


    }

    // =========================================================================
    //          End of TODOs.  Everything below is provided for you.
    //          Read it to understand how your methods are used,
    //          but you should not need to modify it.
    // =========================================================================

    /**
     * Build the ASCII map of the current dungeon state. Squares with a
     * character on them show that character's symbol; empty squares show '.'.
     */
    static String renderMap(int[] xs, int[] ys, int[] types, int[] hp,
                            String label) {
        char[][] grid = new char[DUNGEON_HEIGHT][DUNGEON_WIDTH];
        for (int r = 0; r < DUNGEON_HEIGHT; r++) {
            for (int c = 0; c < DUNGEON_WIDTH; c++) {
                grid[r][c] = '.';
            }
        }
        for (int i = 0; i < xs.length; i++) {
            if (hp[i] > 0 && xs[i] >= 0 && xs[i] < DUNGEON_WIDTH
                          && ys[i] >= 0 && ys[i] < DUNGEON_HEIGHT) {
                grid[ys[i]][xs[i]] = TYPE_SYMBOLS[types[i]];
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- ").append(label).append(" ---\n");
        sb.append('+');
        for (int c = 0; c < DUNGEON_WIDTH; c++) sb.append('-');
        sb.append("+\n");
        for (int r = 0; r < DUNGEON_HEIGHT; r++) {
            sb.append('|');
            for (int c = 0; c < DUNGEON_WIDTH; c++) sb.append(grid[r][c]);
            sb.append("|\n");
        }
        sb.append('+');
        for (int c = 0; c < DUNGEON_WIDTH; c++) sb.append('-');
        sb.append("+\n");
        return sb.toString();
    }

    /**
     * Run a full simulation: a party of three adventurers vs a monster.
     * Each round, every alive character moves toward its enemy and attacks
     * if in range. Returns the full text log.
     */
    static String runSimulation(long seed) {
        Random rng = new Random(seed);

        // ---- Set up party + monster as parallel arrays ----
        // Index 0,1,2 = the party.  Index 3 = the monster.
        String[] names    = { "Aragorn",   "Merlin",    "Shadow",   "Grok the Orc" };
        int[]    types    = { WARRIOR,     WIZARD,      ROGUE,      MONSTER        };
        int[]    xs       = { 1,           2,           1,          18             };
        int[]    ys       = { 1,           2,           3,          8              };
        int[]    hp       = { 25,          15,          20,         40             };
        int[]    strength = { 4,           1,           3,          5              };
        int[]    armor    = { 4,           1,           2,          3              };

        StringBuilder log = new StringBuilder();
        StringBuilder map = new StringBuilder();

        // ---- Encounter intro ----
        log.append("=========================================\n");
        log.append("       D&D ADVENTURE — Lab Simulation     \n");
        log.append("=========================================\n");
        log.append(generateEncounter(3, rng)).append('\n');
        log.append("Heroes: ");
        for (int i = 0; i < 3; i++) {
            log.append(names[i]).append(" the ").append(TYPE_NAMES[types[i]]);
            if (i < 2) log.append(", ");
        }
        log.append(".\n\n");
        map.append(renderMap(xs, ys, types, hp, "Round 0 (Start)")).append('\n');

        // ---- Combat rounds ----
        int round = 1;
        while (round <= MAX_ROUNDS) {
            log.append("--- Round ").append(round).append(" ---\n");

            // Each character takes a turn in order.
            for (int i = 0; i < names.length; i++) {
                if (hp[i] <= 0) continue;  // dead skip

                // Pick a target: party attacks monster, monster attacks first
                // alive party member.
                int target = -1;
                if (types[i] == MONSTER) {
                    for (int j = 0; j < 3; j++) {
                        if (hp[j] > 0) { target = j; break; }
                    }
                } else {
                    if (hp[3] > 0) target = 3;
                }
                if (target < 0) break;

                int speed = SPEEDS[types[i]];
                if (canReach(xs[i], ys[i], xs[target], ys[target], speed + 1)) {
                    // Adjacent or close enough to hit (allow 1 reach beyond move)
                    int dmg = computeAttackDamage(strength[i],
                                                  WEAPON_DICE[types[i]],
                                                  armor[target], rng);
                    hp[target] -= dmg;
                    log.append("  ").append(describeAttack(
                        names[i], types[i], names[target], dmg)).append('\n');
                    if (hp[target] <= 0) {
                        log.append("  ").append(names[target])
                           .append(" has fallen!\n");
                    }
                } else {
                    // Move one step closer
                    int[] np = moveToward(xs[i], ys[i], xs[target], ys[target]);
                    xs[i] = np[0]; ys[i] = np[1];
                    log.append("  ").append(names[i]).append(" the ")
                       .append(TYPE_NAMES[types[i]])
                       .append(" advances to (").append(xs[i]).append(", ")
                       .append(ys[i]).append(").\n");
                }
            }

            map.append(renderMap(xs, ys, types, hp,
                "Round " + round)).append('\n');

            // Check end conditions
            boolean partyAlive = hp[0] > 0 || hp[1] > 0 || hp[2] > 0;
            boolean monsterAlive = hp[3] > 0;
            if (!partyAlive) {
                log.append("\n*** The party has been defeated. ***\n");
                break;
            }
            if (!monsterAlive) {
                log.append("\n*** The party is victorious! ***\n");
                break;
            }
            round++;
        }
        if (round > MAX_ROUNDS) {
            log.append("\n*** Combat continues into legend... (max rounds reached) ***\n");
        }

        log.append("\n--- Final HP ---\n");
        for (int i = 0; i < names.length; i++) {
            log.append(names[i]).append(": ").append(Math.max(0, hp[i]))
               .append(" HP\n");
        }

        // Return BOTH log and map joined with a separator the caller can split on
        return log.toString() + "\n========== DUNGEON MAP ==========\n\n" + map.toString();
    }

    public static void main(String[] args) {
        long seed = (args.length > 0) ? Long.parseLong(args[0]) : 42L;

        String combined = runSimulation(seed);
        int splitAt = combined.indexOf("========== DUNGEON MAP ==========");
        String adventureLog = combined.substring(0, splitAt);
        String dungeonMap   = combined.substring(splitAt);

        try (FileWriter fw = new FileWriter("adventure.txt")) {
            fw.write(adventureLog);
        } catch (IOException e) {
            System.err.println("Could not write adventure.txt: " + e.getMessage());
        }
        try (FileWriter fw = new FileWriter("dungeon.txt")) {
            fw.write(dungeonMap);
        } catch (IOException e) {
            System.err.println("Could not write dungeon.txt: " + e.getMessage());
        }

        System.out.println(adventureLog);
        System.out.println();
        System.out.println("Files written: adventure.txt, dungeon.txt");
    }
}
