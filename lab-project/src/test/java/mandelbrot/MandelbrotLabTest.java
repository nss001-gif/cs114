package mandelbrot;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MandelbrotLabTest — JUnit 5 test suite
 *
 * These tests check each of the five methods you must complete in
 * MandelbrotLab.java.  Run them often as you work through the lab —
 * aim for all tests to pass before you submit.
 *
 * HOW TO RUN (from the project root folder):
 *
 *   Step 1 — compile your code and the tests:
 *     javac -cp lib/junit-platform-console-standalone.jar \
 *           -d out \
 *           src/main/java/mandelbrot/MandelbrotLab.java \
 *           src/test/java/mandelbrot/MandelbrotLabTest.java
 *
 *   Step 2 — run the tests:
 *     java -jar lib/junit-platform-console-standalone.jar \
 *          --class-path out \
 *          --select-class mandelbrot.MandelbrotLabTest
 *
 *   A green summary means all tests passed.
 *   A failing test will show you exactly what went wrong.
 */
@DisplayName("Mandelbrot Lab Tests")
public class MandelbrotLabTest {

    /** Tolerance for floating-point comparisons. */
    static final double DELTA = 1e-9;

    // =========================================================================
    // Part 1 — multiply()
    // =========================================================================

    @Nested
    @DisplayName("Part 1: multiply()")
    class MultiplyTests {

        @Test
        @DisplayName("Basic multiplication: (3+4i) × (1+2i) = -5+10i")
        void basicMultiplication() {
            double[] result = MandelbrotLab.multiply(3, 4, 1, 2);
            assertEquals(2, result.length, "Result array must have length 2");
            assertEquals(-5.0, result[0], DELTA, "Real part should be -5.0");
            assertEquals(10.0, result[1], DELTA, "Imag part should be 10.0");
        }

        @Test
        @DisplayName("Multiply by zero: (3+4i) × (0+0i) = 0+0i")
        void multiplyByZero() {
            double[] result = MandelbrotLab.multiply(3, 4, 0, 0);
            assertEquals(0.0, result[0], DELTA, "Real part should be 0.0");
            assertEquals(0.0, result[1], DELTA, "Imag part should be 0.0");
        }

        @Test
        @DisplayName("Multiply by one: (5+2i) × (1+0i) = 5+2i")
        void multiplyByOne() {
            double[] result = MandelbrotLab.multiply(5, 2, 1, 0);
            assertEquals(5.0, result[0], DELTA, "Real part should be 5.0");
            assertEquals(2.0, result[1], DELTA, "Imag part should be 2.0");
        }

        @Test
        @DisplayName("i × i = -1  (since i² = -1)")
        void iSquaredIsMinusOne() {
            double[] result = MandelbrotLab.multiply(0, 1, 0, 1);
            assertEquals(-1.0, result[0], DELTA, "Real part of i² should be -1.0");
            assertEquals(0.0,  result[1], DELTA, "Imag part of i² should be 0.0");
        }

        @Test
        @DisplayName("Squaring a real number: (3+0i) × (3+0i) = 9+0i")
        void squareRealNumber() {
            double[] result = MandelbrotLab.multiply(3, 0, 3, 0);
            assertEquals(9.0, result[0], DELTA, "Real part should be 9.0");
            assertEquals(0.0, result[1], DELTA, "Imag part should be 0.0");
        }

        @Test
        @DisplayName("Negative components: (-1+2i) × (3+-1i) = -1+5i")
        void negativeComponents() {
            double[] result = MandelbrotLab.multiply(-1, 2, 3, -1);
            // real = (-1)(3) - (2)(-1) = -3 + 2 = -1
            // imag = (-1)(-1) + (2)(3) = 1 + 6 = 7
            assertEquals(-1.0, result[0], DELTA, "Real part should be -1.0");
            assertEquals(7.0,  result[1], DELTA, "Imag part should be 7.0");
        }

        @Test
        @DisplayName("Result array must always have length 2")
        void resultLengthIsTwo() {
            double[] result = MandelbrotLab.multiply(1, 2, 3, 4);
            assertEquals(2, result.length, "multiply() must return an array of length 2");
        }
    }


    // =========================================================================
    // Part 2 — magnitudeSquared()
    // =========================================================================

    @Nested
    @DisplayName("Part 2: magnitudeSquared()")
    class MagnitudeSquaredTests {

        @Test
        @DisplayName("3-4-5 triple: |3+4i|² = 25")
        void classicTriple() {
            assertEquals(25.0, MandelbrotLab.magnitudeSquared(3, 4), DELTA);
        }

        @Test
        @DisplayName("Origin: |0+0i|² = 0")
        void origin() {
            assertEquals(0.0, MandelbrotLab.magnitudeSquared(0, 0), DELTA);
        }

        @Test
        @DisplayName("Unit real: |1+0i|² = 1")
        void unitReal() {
            assertEquals(1.0, MandelbrotLab.magnitudeSquared(1, 0), DELTA);
        }

        @Test
        @DisplayName("Unit imaginary: |0+1i|² = 1")
        void unitImaginary() {
            assertEquals(1.0, MandelbrotLab.magnitudeSquared(0, 1), DELTA);
        }

        @Test
        @DisplayName("Negative inputs: |-3+0i|² = 9  (squaring removes sign)")
        void negativeInputs() {
            assertEquals(9.0, MandelbrotLab.magnitudeSquared(-3, 0), DELTA);
        }

        @Test
        @DisplayName("Result is always non-negative")
        void alwaysNonNegative() {
            assertTrue(MandelbrotLab.magnitudeSquared(-5, -12) >= 0,
                    "magnitudeSquared() must never return a negative number");
        }

        @Test
        @DisplayName("Escape threshold: |1.5+1.5i|² = 4.5 > 4 (should escape)")
        void escapeThreshold() {
            double mag2 = MandelbrotLab.magnitudeSquared(1.5, 1.5);
            assertTrue(mag2 > 4.0,
                    "magnitudeSquared(1.5, 1.5) should be > 4.0 (= " + mag2 + ")");
        }

        @Test
        @DisplayName("No escape: |1+1i|² = 2 < 4 (should not escape yet)")
        void noEscape() {
            double mag2 = MandelbrotLab.magnitudeSquared(1, 1);
            assertTrue(mag2 < 4.0,
                    "magnitudeSquared(1, 1) should be < 4.0 (= " + mag2 + ")");
        }
    }


    // =========================================================================
    // Part 3 — countIterations()
    // =========================================================================

    @Nested
    @DisplayName("Part 3: countIterations()")
    class CountIterationsTests {

        @Test
        @DisplayName("Origin (0,0) never escapes — returns MAX_ITERATIONS")
        void originInSet() {
            assertEquals(MandelbrotLab.MAX_ITERATIONS,
                    MandelbrotLab.countIterations(0.0, 0.0),
                    "c=(0,0) is in the set and should return MAX_ITERATIONS");
        }

        @Test
        @DisplayName("c=(2,0) escapes on the first iteration")
        void escapesImmediately() {
            int result = MandelbrotLab.countIterations(2.0, 0.0);
            assertTrue(result < MandelbrotLab.MAX_ITERATIONS,
                    "c=(2,0) should escape before MAX_ITERATIONS, got: " + result);
            assertTrue(result >= 1,
                    "countIterations() must return at least 1 (got " + result + ")");
        }

        @Test
        @DisplayName("c=(-1,0) is in the set (period-2 orbit) — returns MAX_ITERATIONS")
        void period2BulbInSet() {
            assertEquals(MandelbrotLab.MAX_ITERATIONS,
                    MandelbrotLab.countIterations(-1.0, 0.0),
                    "c=(-1,0) is in the Mandelbrot set");
        }

        @Test
        @DisplayName("c=(0.5,0.5) escapes — result is less than MAX_ITERATIONS")
        void escapesEventually() {
            int result = MandelbrotLab.countIterations(0.5, 0.5);
            assertTrue(result < MandelbrotLab.MAX_ITERATIONS,
                    "c=(0.5,0.5) should escape (got MAX_ITERATIONS — check your escape condition)");
        }

        @Test
        @DisplayName("c=(3,0) escapes quickly — result is a small number")
        void escapesQuickly() {
            int result = MandelbrotLab.countIterations(3.0, 0.0);
            assertTrue(result < 10,
                    "c=(3,0) should escape in fewer than 10 iterations (got " + result + ")");
        }

        @Test
        @DisplayName("Return value is always in range [1, MAX_ITERATIONS]")
        void returnValueInRange() {
            int result = MandelbrotLab.countIterations(-0.75, 0.1);
            assertTrue(result >= 1 && result <= MandelbrotLab.MAX_ITERATIONS,
                    "countIterations() must return a value in [1, MAX_ITERATIONS], got: " + result);
        }

        @Test
        @DisplayName("c=(-2.5,0) is outside the set — escapes")
        void farLeft() {
            int result = MandelbrotLab.countIterations(-2.5, 0.0);
            assertTrue(result < MandelbrotLab.MAX_ITERATIONS,
                    "c=(-2.5,0) should escape (got MAX_ITERATIONS)");
        }
    }


    // =========================================================================
    // Part 4 — pixelToComplex()
    // =========================================================================

    @Nested
    @DisplayName("Part 4: pixelToComplex()")
    class PixelToComplexTests {

        @Test
        @DisplayName("Top-left pixel (0,0) maps to (X_MIN, Y_MAX)")
        void topLeftCorner() {
            double[] result = MandelbrotLab.pixelToComplex(0, 0);
            assertEquals(2, result.length, "Result must have length 2");
            assertEquals(MandelbrotLab.X_MIN, result[0], DELTA,
                    "Pixel (0,0) real part should equal X_MIN");
            assertEquals(MandelbrotLab.Y_MAX, result[1], DELTA,
                    "Pixel (0,0) imag part should equal Y_MAX");
        }

        @Test
        @DisplayName("Bottom-right pixel (800,600) maps to (X_MAX, Y_MIN)")
        void bottomRightCorner() {
            double[] result = MandelbrotLab.pixelToComplex(800, 600);
            assertEquals(MandelbrotLab.X_MAX, result[0], DELTA,
                    "Pixel (800,600) real part should equal X_MAX");
            assertEquals(MandelbrotLab.Y_MIN, result[1], DELTA,
                    "Pixel (800,600) imag part should equal Y_MIN");
        }

        @Test
        @DisplayName("Center pixel (400,300) maps to approximately (-0.75, 0.0)")
        void centerPixel() {
            double[] result = MandelbrotLab.pixelToComplex(400, 300);
            assertEquals(-0.75, result[0], 0.01,
                    "Center pixel real part should be near -0.75");
            assertEquals(0.0,   result[1], 0.01,
                    "Center pixel imag part should be near 0.0");
        }

        @Test
        @DisplayName("y-axis is flipped: increasing py decreases cImag")
        void yAxisFlipped() {
            double[] top    = MandelbrotLab.pixelToComplex(400, 0);
            double[] bottom = MandelbrotLab.pixelToComplex(400, 599);
            assertTrue(top[1] > bottom[1],
                    "Row 0 (top) should have a larger imaginary part than row 599 (bottom)");
        }

        @Test
        @DisplayName("Increasing px increases cReal (left to right)")
        void xAxisCorrectDirection() {
            double[] left  = MandelbrotLab.pixelToComplex(0,   300);
            double[] right = MandelbrotLab.pixelToComplex(799, 300);
            assertTrue(right[0] > left[0],
                    "A pixel on the right should have a larger real part than one on the left");
        }

        @Test
        @DisplayName("Result array must always have length 2")
        void resultLengthIsTwo() {
            double[] result = MandelbrotLab.pixelToComplex(100, 200);
            assertEquals(2, result.length,
                    "pixelToComplex() must return an array of length 2");
        }
    }


    // =========================================================================
    // Part 5 — getColor()
    // =========================================================================

    @Nested
    @DisplayName("Part 5: getColor()")
    class GetColorTests {

        @Test
        @DisplayName("In-set pixels (MAX_ITERATIONS) must return black (0x000000)")
        void inSetIsBlack() {
            int color = MandelbrotLab.getColor(MandelbrotLab.MAX_ITERATIONS);
            assertEquals(0x000000, color,
                    "Points in the set must be colored black (0x000000)");
        }

        @Test
        @DisplayName("Color value must fit in a 24-bit RGB int (0x000000 to 0xFFFFFF)")
        void colorInValidRange() {
            int color = MandelbrotLab.getColor(50);
            assertTrue(color >= 0x000000 && color <= 0xFFFFFF,
                    "getColor() must return a value between 0x000000 and 0xFFFFFF (got " +
                            Integer.toHexString(color) + ")");
        }

        @Test
        @DisplayName("Red channel must be in 0-255 (bits 23-16)")
        void redChannelInRange() {
            int color = MandelbrotLab.getColor(100);
            int red = (color >> 16) & 0xFF;
            assertTrue(red >= 0 && red <= 255,
                    "Red channel out of range: " + red);
        }

        @Test
        @DisplayName("Green channel must be in 0-255 (bits 15-8)")
        void greenChannelInRange() {
            int color = MandelbrotLab.getColor(100);
            int green = (color >> 8) & 0xFF;
            assertTrue(green >= 0 && green <= 255,
                    "Green channel out of range: " + green);
        }

        @Test
        @DisplayName("Blue channel must be in 0-255 (bits 7-0)")
        void blueChannelInRange() {
            int color = MandelbrotLab.getColor(100);
            int blue = color & 0xFF;
            assertTrue(blue >= 0 && blue <= 255,
                    "Blue channel out of range: " + blue);
        }

        @Test
        @DisplayName("Escaped pixels (not MAX_ITERATIONS) should not all be black")
        void escapedPixelsNotAllBlack() {
            // At least one escape iteration count should produce a non-black color.
            // If all escaped pixels are black, the image will be all black — check your logic.
            boolean anyNonBlack = false;
            for (int i = 1; i < MandelbrotLab.MAX_ITERATIONS; i++) {
                if (MandelbrotLab.getColor(i) != 0x000000) {
                    anyNonBlack = true;
                    break;
                }
            }
            assertTrue(anyNonBlack,
                    "At least one escaped iteration count must produce a non-black color. " +
                            "Check that your if/else handles iterations < MAX_ITERATIONS correctly.");
        }
    }
}
