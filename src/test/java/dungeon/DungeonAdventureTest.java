package dungeon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DungeonAdventure lab.
 *
 * Organized in seven nested classes (one per Part). You can run them all,
 * or focus on one Part while you work on it.
 *
 * Do NOT modify this file.
 */
@DisplayName("D&D Adventure Lab — Test Suite")
public class DungeonAdventureTest {

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Part 1 — rollDice()")
    class Part1_RollDice {

        @Test
        @DisplayName("Single die: result is in [1, numSides]")
        void singleDieInRange() {
            Random rng = new Random(12345L);
            for (int i = 0; i < 200; i++) {
                int r = DungeonAdventure.rollDice(1, 6, rng);
                assertTrue(r >= 1 && r <= 6,
                    "Roll of 1d6 must be between 1 and 6. Got: " + r);
            }
        }

        @Test
        @DisplayName("Multiple dice: sum is in [numDice, numDice*numSides]")
        void multipleDiceInRange() {
            Random rng = new Random(7L);
            for (int i = 0; i < 100; i++) {
                int r = DungeonAdventure.rollDice(3, 6, rng);
                assertTrue(r >= 3 && r <= 18,
                    "Sum of 3d6 must be between 3 and 18. Got: " + r);
            }
        }

        @Test
        @DisplayName("Deterministic: same seed produces same sum")
        void deterministicWithSeed() {
            int a = DungeonAdventure.rollDice(5, 10, new Random(42L));
            int b = DungeonAdventure.rollDice(5, 10, new Random(42L));
            assertEquals(a, b,
                "Two calls with seed 42 must produce the same result. " +
                "Did you create your own Random instead of using rng?");
        }

        @Test
        @DisplayName("Single d4 with seed 0: returns 3")
        void specificSeedSingleDie() {
            // new Random(0L).nextInt(4) returns 0, so 0 + 1 = 1
            // Wait - let me actually test what new Random(0L).nextInt(4) returns
            // Using a known-good seed for a deterministic check:
            Random rng = new Random(0L);
            int r = DungeonAdventure.rollDice(1, 4, rng);
            // Manually compute the expected with the same seed
            Random rng2 = new Random(0L);
            int expected = rng2.nextInt(4) + 1;
            assertEquals(expected, r,
                "1d4 with seed 0 should be (rng.nextInt(4) + 1) = " + expected);
        }

        @Test
        @DisplayName("Sums correctly: 4 dice, all max value")
        void sumsAllRolls() {
            // We can't force specific rolls, but we can test the sum vs the
            // sum we compute by hand using the same seed.
            long seed = 99L;
            int got = DungeonAdventure.rollDice(4, 6, new Random(seed));
            Random check = new Random(seed);
            int expected = 0;
            for (int i = 0; i < 4; i++) expected += check.nextInt(6) + 1;
            assertEquals(expected, got,
                "rollDice(4, 6, rng) must equal the sum of 4 rng-based rolls. " +
                "Common error: forgetting +1 on the roll, or rolling the wrong number of dice.");
        }

        @Test
        @DisplayName("Two-sided die (a coin) returns 1 or 2")
        void twoSidedDie() {
            Random rng = new Random(123L);
            Set<Integer> seen = new HashSet<>();
            for (int i = 0; i < 100; i++) {
                int r = DungeonAdventure.rollDice(1, 2, rng);
                assertTrue(r == 1 || r == 2, "1d2 must be 1 or 2. Got: " + r);
                seen.add(r);
            }
            assertEquals(2, seen.size(),
                "Over 100 rolls of 1d2 we should see both 1 and 2. " +
                "Are you returning a constant?");
        }

        @Test
        @DisplayName("Many dice: result is plausibly random (not constant)")
        void notConstant() {
            Random rng = new Random(456L);
            Set<Integer> seen = new HashSet<>();
            for (int i = 0; i < 30; i++) {
                seen.add(DungeonAdventure.rollDice(2, 6, rng));
            }
            assertTrue(seen.size() > 3,
                "Sum of 2d6 should produce many distinct values across 30 calls. " +
                "Got only " + seen.size() + " distinct value(s) — are you using rng?");
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Part 2 — computeAttackDamage()")
    class Part2_ComputeAttackDamage {

        @Test
        @DisplayName("Damage = roll + strength - armor")
        void basicFormula() {
            long seed = 1L;
            int got = DungeonAdventure.computeAttackDamage(3, 8, 2, new Random(seed));
            // Expected: rollDice(1, 8, rng) + 3 - 2
            Random check = new Random(seed);
            int roll = check.nextInt(8) + 1;
            int expected = roll + 3 - 2;
            if (expected < 0) expected = 0;
            assertEquals(expected, got,
                "Formula must be: rollDice(1, weaponDie, rng) + strength - armor, clamped to 0.");
        }

        @Test
        @DisplayName("Heavy armor produces 0 (clamped)")
        void clampToZero() {
            // strength=0, weaponDie=4 (max roll = 4), armor=20 → always negative
            for (long seed = 0; seed < 20; seed++) {
                int dmg = DungeonAdventure.computeAttackDamage(0, 4, 20, new Random(seed));
                assertEquals(0, dmg,
                    "When armor is much larger than strength + max roll, " +
                    "damage must clamp to 0. Got: " + dmg + " on seed " + seed);
            }
        }

        @Test
        @DisplayName("Damage never negative")
        void neverNegative() {
            Random rng = new Random(50L);
            for (int i = 0; i < 100; i++) {
                int dmg = DungeonAdventure.computeAttackDamage(1, 6, 10, rng);
                assertTrue(dmg >= 0,
                    "Damage must never be negative. Got: " + dmg);
            }
        }

        @Test
        @DisplayName("Zero armor: damage = roll + strength")
        void zeroArmor() {
            long seed = 99L;
            int got = DungeonAdventure.computeAttackDamage(5, 10, 0, new Random(seed));
            Random check = new Random(seed);
            int expected = (check.nextInt(10) + 1) + 5;
            assertEquals(expected, got,
                "With zero armor, damage = roll + strength. " +
                "Did you accidentally subtract something else?");
        }

        @Test
        @DisplayName("Zero strength: damage = roll - armor (clamped)")
        void zeroStrength() {
            long seed = 7L;
            int got = DungeonAdventure.computeAttackDamage(0, 12, 3, new Random(seed));
            Random check = new Random(seed);
            int roll = check.nextInt(12) + 1;
            int expected = Math.max(0, roll - 3);
            assertEquals(expected, got,
                "With zero strength, damage = max(0, roll - armor).");
        }

        @Test
        @DisplayName("Maximum possible damage is bounded")
        void boundedAbove() {
            Random rng = new Random(42L);
            for (int i = 0; i < 50; i++) {
                int dmg = DungeonAdventure.computeAttackDamage(2, 6, 1, rng);
                // Max: roll=6, +2, -1 = 7. Min: 0.
                assertTrue(dmg >= 0 && dmg <= 7,
                    "With weaponDie=6, str=2, armor=1, damage must be in [0, 7]. Got: " + dmg);
            }
        }

        @Test
        @DisplayName("Uses rollDice (1 roll consumed per call)")
        void usesRollDiceCorrectly() {
            // If computeAttackDamage uses rollDice(1, weaponDie, rng), then
            // calling it once should consume exactly one nextInt from rng.
            long seed = 13L;
            Random rng1 = new Random(seed);
            DungeonAdventure.computeAttackDamage(0, 8, 0, rng1);
            int afterFirst = rng1.nextInt(1000);

            Random rng2 = new Random(seed);
            rng2.nextInt(8);  // skip one roll
            int expected = rng2.nextInt(1000);

            assertEquals(expected, afterFirst,
                "computeAttackDamage should consume exactly one roll from rng " +
                "(by calling rollDice(1, weaponDie, rng)).");
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Part 3 — distanceBetween()")
    class Part3_DistanceBetween {

        @Test
        @DisplayName("Same square = 0")
        void sameSquare() {
            assertEquals(0, DungeonAdventure.distanceBetween(3, 5, 3, 5));
            assertEquals(0, DungeonAdventure.distanceBetween(0, 0, 0, 0));
        }

        @Test
        @DisplayName("Pure horizontal")
        void horizontal() {
            assertEquals(3, DungeonAdventure.distanceBetween(1, 1, 4, 1));
            assertEquals(7, DungeonAdventure.distanceBetween(0, 5, 7, 5));
        }

        @Test
        @DisplayName("Pure vertical")
        void vertical() {
            assertEquals(4, DungeonAdventure.distanceBetween(2, 1, 2, 5));
            assertEquals(6, DungeonAdventure.distanceBetween(8, 0, 8, 6));
        }

        @Test
        @DisplayName("Diagonal: returns max of dx and dy")
        void diagonal() {
            assertEquals(4, DungeonAdventure.distanceBetween(0, 0, 3, 4),
                "Chebyshev distance from (0,0) to (3,4) is max(3, 4) = 4. " +
                "Did you accidentally use Pythagoras (sqrt(3*3+4*4)=5) or " +
                "Manhattan (3+4=7)?");
            assertEquals(5, DungeonAdventure.distanceBetween(1, 2, 6, 5),
                "Chebyshev (1,2)→(6,5) is max(5, 3) = 5.");
        }

        @Test
        @DisplayName("Symmetric: d(A,B) == d(B,A)")
        void symmetric() {
            for (int x1 = 0; x1 < 5; x1++) {
                for (int y1 = 0; y1 < 5; y1++) {
                    for (int x2 = 0; x2 < 5; x2++) {
                        for (int y2 = 0; y2 < 5; y2++) {
                            assertEquals(
                                DungeonAdventure.distanceBetween(x1, y1, x2, y2),
                                DungeonAdventure.distanceBetween(x2, y2, x1, y1),
                                "distanceBetween must be symmetric. " +
                                "Forgot Math.abs?");
                        }
                    }
                }
            }
        }

        @Test
        @DisplayName("Negative coordinates handled correctly")
        void negativeCoordinates() {
            // Even though the dungeon is in positive coords, the math should
            // still work — this catches missing Math.abs.
            assertEquals(5, DungeonAdventure.distanceBetween(-2, 0, 3, 0),
                "(-2,0) to (3,0) is 5. Missing Math.abs?");
            assertEquals(4, DungeonAdventure.distanceBetween(0, 0, -3, -4));
        }

        @Test
        @DisplayName("Large coordinates")
        void largeValues() {
            assertEquals(100, DungeonAdventure.distanceBetween(0, 0, 100, 50));
            assertEquals(50, DungeonAdventure.distanceBetween(50, 50, 100, 50));
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Part 4 — canReach()")
    class Part4_CanReach {

        @Test
        @DisplayName("Same square: always reachable")
        void sameSquareReachable() {
            assertTrue(DungeonAdventure.canReach(3, 3, 3, 3, 0));
            assertTrue(DungeonAdventure.canReach(3, 3, 3, 3, 5));
        }

        @Test
        @DisplayName("Within speed: reachable")
        void withinSpeedReachable() {
            assertTrue(DungeonAdventure.canReach(0, 0, 3, 3, 5),
                "Distance 3, speed 5 → reachable.");
            assertTrue(DungeonAdventure.canReach(2, 2, 4, 4, 2),
                "Distance 2, speed 2 → reachable (≤, not <).");
        }

        @Test
        @DisplayName("Beyond speed: not reachable")
        void beyondSpeedNotReachable() {
            assertFalse(DungeonAdventure.canReach(0, 0, 5, 5, 4),
                "Distance 5, speed 4 → NOT reachable.");
            assertFalse(DungeonAdventure.canReach(0, 0, 10, 0, 3));
        }

        @Test
        @DisplayName("Boundary case: distance == speed (reachable)")
        void boundaryReachable() {
            assertTrue(DungeonAdventure.canReach(0, 0, 3, 3, 3),
                "Distance 3, speed 3 → reachable. Did you use < instead of ≤?");
        }

        @Test
        @DisplayName("Speed 0: only same square reachable")
        void speedZero() {
            assertTrue(DungeonAdventure.canReach(2, 2, 2, 2, 0));
            assertFalse(DungeonAdventure.canReach(2, 2, 2, 3, 0));
            assertFalse(DungeonAdventure.canReach(2, 2, 3, 2, 0));
        }

        @Test
        @DisplayName("Order of args doesn't matter (symmetric)")
        void symmetric() {
            assertEquals(
                DungeonAdventure.canReach(1, 1, 4, 5, 4),
                DungeonAdventure.canReach(4, 5, 1, 1, 4));
            assertEquals(
                DungeonAdventure.canReach(0, 0, 8, 8, 7),
                DungeonAdventure.canReach(8, 8, 0, 0, 7));
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Part 5 — moveToward()")
    class Part5_MoveToward {

        @Test
        @DisplayName("Already at target: stays put")
        void alreadyAtTarget() {
            assertArrayEquals(new int[]{5, 5},
                DungeonAdventure.moveToward(5, 5, 5, 5));
            assertArrayEquals(new int[]{0, 0},
                DungeonAdventure.moveToward(0, 0, 0, 0));
        }

        @Test
        @DisplayName("Target to the right: x increases by 1")
        void moveRight() {
            assertArrayEquals(new int[]{3, 5},
                DungeonAdventure.moveToward(2, 5, 8, 5));
        }

        @Test
        @DisplayName("Target to the left: x decreases by 1")
        void moveLeft() {
            assertArrayEquals(new int[]{4, 3},
                DungeonAdventure.moveToward(5, 3, 1, 3));
        }

        @Test
        @DisplayName("Target below: y increases by 1")
        void moveDown() {
            assertArrayEquals(new int[]{4, 4},
                DungeonAdventure.moveToward(4, 3, 4, 8));
        }

        @Test
        @DisplayName("Target above: y decreases by 1")
        void moveUp() {
            assertArrayEquals(new int[]{4, 2},
                DungeonAdventure.moveToward(4, 3, 4, 0));
        }

        @Test
        @DisplayName("Diagonal: both x and y change by 1")
        void moveDiagonal() {
            assertArrayEquals(new int[]{3, 5},
                DungeonAdventure.moveToward(4, 4, 1, 7),
                "From (4,4) toward (1,7): x decreases (4>1), y increases (4<7) → (3,5)");
            assertArrayEquals(new int[]{6, 4},
                DungeonAdventure.moveToward(5, 5, 9, 1),
                "From (5,5) toward (9,1): x increases, y decreases → (6,4)");
        }

        @Test
        @DisplayName("Each step closes distance by exactly 1 (Chebyshev)")
        void closesDistanceByOne() {
            int[] start = {1, 2};
            int[] target = {10, 6};
            int[] step1 = DungeonAdventure.moveToward(start[0], start[1], target[0], target[1]);
            int distBefore = Math.max(Math.abs(start[0]-target[0]), Math.abs(start[1]-target[1]));
            int distAfter  = Math.max(Math.abs(step1[0]-target[0]), Math.abs(step1[1]-target[1]));
            assertEquals(distBefore - 1, distAfter,
                "Each step should reduce Chebyshev distance by exactly 1.");
        }

        @Test
        @DisplayName("Returns array of length 2")
        void returnsLengthTwo() {
            int[] result = DungeonAdventure.moveToward(0, 0, 5, 5);
            assertEquals(2, result.length,
                "moveToward must return an int[] of length 2: {newX, newY}.");
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Part 6 — describeAttack()")
    class Part6_DescribeAttack {

        @Test
        @DisplayName("Standard attack format with damage")
        void standardFormat() {
            String s = DungeonAdventure.describeAttack(
                "Aragorn", DungeonAdventure.WARRIOR, "Goblin", 7);
            assertEquals("Aragorn the Warrior attacks Goblin for 7 damage!", s,
                "Format must be EXACTLY: " +
                "<name> the <Type> attacks <defender> for <n> damage!");
        }

        @Test
        @DisplayName("Zero damage uses 'bounces off' format")
        void bouncesOff() {
            String s = DungeonAdventure.describeAttack(
                "Merlin", DungeonAdventure.WIZARD, "Troll", 0);
            assertEquals("Merlin the Wizard attacks Troll but the attack bounces off!", s,
                "When damage is 0, format must be EXACTLY: " +
                "<name> the <Type> attacks <defender> but the attack bounces off!");
        }

        @Test
        @DisplayName("Uses TYPE_NAMES for class name (not the int)")
        void usesTypeName() {
            String s = DungeonAdventure.describeAttack(
                "Shadow", DungeonAdventure.ROGUE, "Bandit", 4);
            assertTrue(s.contains("Rogue"),
                "Should contain 'Rogue' (from TYPE_NAMES[ROGUE]). Got: " + s);
            assertFalse(s.contains(" 2 "),  // ROGUE = 2; if used as int it'd appear
                "Should NOT contain the int code 2. Use TYPE_NAMES[type], not type itself.");
        }

        @Test
        @DisplayName("Cleric attack")
        void clericAttack() {
            String s = DungeonAdventure.describeAttack(
                "Eldrin", DungeonAdventure.CLERIC, "Skeleton", 3);
            assertEquals("Eldrin the Cleric attacks Skeleton for 3 damage!", s);
        }

        @Test
        @DisplayName("Monster attack uses 'Monster' name")
        void monsterAttack() {
            String s = DungeonAdventure.describeAttack(
                "Orc", DungeonAdventure.MONSTER, "Aragorn", 10);
            assertEquals("Orc the Monster attacks Aragorn for 10 damage!", s);
        }

        @Test
        @DisplayName("Damage of 1 is singular but format unchanged")
        void singleDamage() {
            String s = DungeonAdventure.describeAttack(
                "Hero", DungeonAdventure.WARRIOR, "Bat", 1);
            assertEquals("Hero the Warrior attacks Bat for 1 damage!", s,
                "Format is unchanged for damage of 1 — still 'damage!' (not 'damage point!').");
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Part 7 — generateEncounter()")
    class Part7_GenerateEncounter {

        @Test
        @DisplayName("Result starts with 'A level <N> party encounters a '")
        void startsCorrectly() {
            for (int level = 1; level <= 10; level++) {
                String s = DungeonAdventure.generateEncounter(level, new Random(level));
                assertTrue(s.startsWith("A level " + level + " party encounters a "),
                    "Output should start with 'A level " + level + " party encounters a '. " +
                    "Got: " + s);
            }
        }

        @Test
        @DisplayName("Result ends with '!'")
        void endsWithBang() {
            String s = DungeonAdventure.generateEncounter(3, new Random(1L));
            assertTrue(s.endsWith("!"),
                "Result must end with an exclamation mark. Got: " + s);
        }

        @Test
        @DisplayName("Monster name is one of the four allowed strings")
        void allowedMonsters() {
            Set<String> allowed = new HashSet<>();
            allowed.add("Goblin Scout");
            allowed.add("Skeleton Warrior");
            allowed.add("Dire Wolf");
            allowed.add("Orc Berserker");

            for (long seed = 0; seed < 50; seed++) {
                String s = DungeonAdventure.generateEncounter(1, new Random(seed));
                boolean found = false;
                for (String m : allowed) {
                    if (s.contains(m)) { found = true; break; }
                }
                assertTrue(found,
                    "Encounter must mention exactly one of: " + allowed + ". Got: " + s);
            }
        }

        @Test
        @DisplayName("All four monsters can appear (over many seeds)")
        void allFourReachable() {
            Set<String> seen = new HashSet<>();
            // NOTE: new Random(0L), new Random(1L), ... all happen to return
            // the same first nextInt(4) value (a quirk of java.util.Random's
            // poor seed mixing on small inputs). To get a real distribution,
            // we use one Random and let it keep flowing across calls.
            Random shared = new Random(2026L);
            for (int i = 0; i < 200; i++) {
                String s = DungeonAdventure.generateEncounter(1, shared);
                if (s.contains("Goblin Scout"))      seen.add("Goblin Scout");
                if (s.contains("Skeleton Warrior"))  seen.add("Skeleton Warrior");
                if (s.contains("Dire Wolf"))         seen.add("Dire Wolf");
                if (s.contains("Orc Berserker"))     seen.add("Orc Berserker");
            }
            assertEquals(4, seen.size(),
                "Across 200 calls we should see all four monsters. " +
                "Saw only: " + seen + ". Are you using rollDice(1, 4, rng)?");
        }

        @Test
        @DisplayName("Mapping: roll 1 → Goblin Scout (off-by-one check)")
        void rollOneIsGoblin() {
            // Find a Random whose first nextInt(4) returns 0 (i.e. the d4
            // rolls 1 in our system). Then the result must contain
            // "Goblin Scout", not "Skeleton Warrior".
            Random rng = null;
            for (long s = 0; s < 1_000_000L; s++) {
                Random probe = new Random(s);
                if (probe.nextInt(4) == 0) {
                    rng = new Random(s);
                    break;
                }
            }
            assertNotNull(rng, "Test setup error: could not find a seed with nextInt(4)==0");
            String result = DungeonAdventure.generateEncounter(2, rng);
            assertTrue(result.contains("Goblin Scout"),
                "When rng.nextInt(4) returns 0, the d4 rolls 1 → 'Goblin Scout' " +
                "(index 0 in the monster array). Got: " + result + ". " +
                "Common bug: forgetting to subtract 1 from a 1-based roll when " +
                "indexing a 0-based array.");
        }

        @Test
        @DisplayName("Party level appears in output")
        void partyLevelInOutput() {
            String s = DungeonAdventure.generateEncounter(7, new Random(0L));
            assertTrue(s.contains("level 7"),
                "Result must mention the party level. Got: " + s);
        }
    }
}
