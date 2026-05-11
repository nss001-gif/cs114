package mandelbrot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * CS Lab: Mandelbrot Set
 *
 * In this lab you will build a program that draws the Mandelbrot set —
 * a famous fractal image produced entirely by a simple math rule.
 *
 * You will complete FIVE methods, marked with TODO. Read each method's
 * comment carefully: it tells you exactly what the method receives,
 * what it must calculate, and what it must return.
 *
 * DO NOT change any method signatures (names, parameter types, return types).
 * DO NOT change the main() method — it calls your methods to produce the image.
 *
 * How to compile and run from the project root:
 *   javac -d out src/main/java/mandelbrot/MandelbrotLab.java
 *   java  -cp out mandelbrot.MandelbrotLab
 *
 * If everything is correct, a file called "mandelbrot.png" will appear
 * in the project folder. Open it to see your fractal!
 *
 * @author [Your name here]
 */
public class MandelbrotLab {

    // -------------------------------------------------------------------------
    // Constants — do not change these values
    // -------------------------------------------------------------------------

    /** Maximum number of iterations before we decide a point is "in" the set. */
    public static final int MAX_ITERATIONS = 200;

    /** The image will be this many pixels wide. */
    static final int IMAGE_WIDTH = 800;

    /** The image will be this many pixels tall. */
    static final int IMAGE_HEIGHT = 600;

    /** Left edge of the region of the complex plane we are drawing. */
    public static final double X_MIN = -2.5;

    /** Right edge of the region of the complex plane we are drawing. */
    public static final double X_MAX = 1.0;

    /** Bottom edge of the region of the complex plane we are drawing. */
    public static final double Y_MIN = -1.2;

    /** Top edge of the region of the complex plane we are drawing. */
    public static final double Y_MAX = 1.2;

    // =========================================================================
    // PART 1 — Complex number multiplication
    // =========================================================================

    /**
     * Multiplies two complex numbers together and returns the result as a
     * two-element array.
     *
     * BACKGROUND:
     *   A complex number looks like  a + bi
     *   where  a  is the "real" part and  b  is the "imaginary" part.
     *   The letter  i  represents the square root of -1.
     *
     *   To multiply two complex numbers (a + bi) × (c + di):
     *     real part of result  =  a*c - b*d
     *     imag part of result  =  a*d + b*c
     *
     * EXAMPLE:
     *   multiply(3, 4, 1, 2)  →  [3*1 - 4*2,  3*2 + 4*1]  =  [-5.0, 10.0]
     *   multiply(1, 0, 1, 0)  →  [1*1 - 0*0,  1*0 + 0*1]  =  [1.0,  0.0]
     *
     * @param aReal  real part of the first complex number
     * @param aImag  imaginary part of the first complex number
     * @param bReal  real part of the second complex number
     * @param bImag  imaginary part of the second complex number
     * @return       a double[] of length 2 where:
     *                 result[0] = real part of the product
     *                 result[1] = imaginary part of the product
     */
    public static double[] multiply(double aReal, double aImag, double bReal, double bImag) {
        // TODO: Calculate the real part of the product (a*c - b*d)
        double realPart = aReal * bReal - aImag * bImag; // replace 0 with your calculation

        // TODO: Calculate the imaginary part of the product (a*d + b*c)
        double imagPart = aReal * bImag + aImag * bReal; // replace 0 with your calculation

        return new double[]{realPart, imagPart};
    }


    // =========================================================================
    // PART 2 — Magnitude (distance from zero)
    // =========================================================================

    /**
     * Calculates the squared magnitude of a complex number.
     *
     * BACKGROUND:
     *   The "magnitude" of a complex number a + bi is how far it is from zero.
     *   The formula is:
     *
     *     magnitude        =  sqrt(a² + b²)
     *     magnitude²       =  a² + b²
     *
     *   We use the SQUARED magnitude (no square root) because:
     *     - It is faster to compute (sqrt is slow)
     *     - We only need to compare to 4.0, which equals 2.0²
     *       So "magnitude > 2" is the same as "magnitude² > 4"
     *
     * EXAMPLE:
     *   magnitudeSquared(3, 4)  →  3² + 4²  =  9 + 16  =  25.0
     *   magnitudeSquared(0, 0)  →  0² + 0²  =  0.0
     *   magnitudeSquared(1, 1)  →  1² + 1²  =  2.0
     *
     * @param real  real part of the complex number
     * @param imag  imaginary part of the complex number
     * @return      real² + imag²  (a double, always >= 0)
     */
    public static double magnitudeSquared(double real, double imag) {
        // TODO: Return real² + imag²
        // Hint: in Java, real*real is real squared (or use Math.pow(real, 2))
        return real * real + imag * imag; // replace this line
    }


    // =========================================================================
    // PART 3 — Counting iterations (the core Mandelbrot rule)
    // =========================================================================

    /**
     * Applies the Mandelbrot rule to a point and returns how many steps it
     * takes before the sequence "escapes" (grows without bound).
     *
     * BACKGROUND:
     *   For any complex number c, we start at z = 0 and keep repeating:
     *
     *       z  →  z² + c
     *
     *   If the magnitude of z ever exceeds 2.0, the sequence will grow forever
     *   (it "escapes"). We return how many steps that took.
     *
     *   If after MAX_ITERATIONS steps z still hasn't escaped, we decide the
     *   point is probably IN the Mandelbrot set and return MAX_ITERATIONS.
     *
     * HOW TO COMPUTE z² + c:
     *   z      is a complex number with parts  (zReal, zImag)
     *   c      is a complex number with parts  (cReal, cImag)
     *   z²     = multiply(zReal, zImag, zReal, zImag)
     *   z² + c = add the result of multiply to (cReal, cImag)
     *
     *   Adding two complex numbers is simple:
     *     real part: zSquaredReal + cReal
     *     imag part: zSquaredImag + cImag
     *
     * ESCAPE CHECK:
     *   After each step, call magnitudeSquared(zReal, zImag).
     *   If the result > 4.0, the point has escaped → return the step count.
     *
     * EXAMPLES (with MAX_ITERATIONS = 200):
     *   countIterations(0.0,  0.0)  →  200  (origin is in the set)
     *   countIterations(2.0,  0.0)  →  1    (escapes on first step)
     *   countIterations(0.5,  0.5)  →  5    (escapes after 5 steps)
     *
     * @param cReal  real part of the point c being tested
     * @param cImag  imaginary part of the point c being tested
     * @return       the number of iterations before escape,
     *               or MAX_ITERATIONS if the point did not escape
     */
    public static int countIterations(double cReal, double cImag) {
        // Start with z = 0 + 0i
        double zReal = 0.0;
        double zImag = 0.0;

        // TODO: Write a loop that runs up to MAX_ITERATIONS times.
        //
        //   Each time through the loop:
        //     1. Use multiply() to compute z² (multiply z by itself)
        //     2. Add cReal to the real part of z²  → new zReal
        //        Add cImag to the imag part of z²  → new zImag
        //     3. Use magnitudeSquared() to check if z has escaped (> 4.0)
        //     4. If it has escaped, return the current loop counter
        //
        //   If the loop finishes without escaping, return MAX_ITERATIONS.

        for (int i = 1; i <=MAX_ITERATIONS; i++){
            double[] zSquared = multiply(zReal, zImag, zReal, zImag);
            zReal = zSquared[0] + cReal;
            zImag = zSquared[1] + cImag;
            if(magnitudeSquared(zReal, zImag) > 4.0) {
                return i;
            }
        }

        return MAX_ITERATIONS; // placeholder — replace this
    }


    // =========================================================================
    // PART 4 — Mapping a pixel to a point in the complex plane
    // =========================================================================

    /**
     * Converts a pixel position on screen to a complex number (c) that we can
     * test with countIterations().
     *
     * BACKGROUND:
     *   Our image is IMAGE_WIDTH × IMAGE_HEIGHT pixels.
     *   The Mandelbrot set lives in a region of the complex plane:
     *     x (real axis): from X_MIN to X_MAX
     *     y (imag axis): from Y_MIN to Y_MAX
     *
     *   We need to "stretch" pixel coordinates onto that region.
     *
     * FORMULA:
     *   For pixel column px (0 = left edge):
     *     cReal = X_MIN  +  (px / IMAGE_WIDTH)  * (X_MAX - X_MIN)
     *
     *   For pixel row py (0 = TOP edge):
     *     cImag = Y_MAX  -  (py / IMAGE_HEIGHT) * (Y_MAX - Y_MIN)
     *     NOTE: py=0 is the top of the image, but Y_MAX is the mathematical top,
     *     so we subtract to flip the axis correctly.
     *
     * IMPORTANT — integer division trap:
     *   px and py are int values. In Java, int/int gives an int (truncated).
     *   Write  (double) px / IMAGE_WIDTH  to force a decimal result.
     *
     * EXAMPLE (IMAGE_WIDTH=800, IMAGE_HEIGHT=600):
     *   pixelToComplex(0,   0)    →  [-2.5,  1.2]   (top-left corner)
     *   pixelToComplex(800, 600)  →  [ 1.0, -1.2]   (bottom-right corner)
     *   pixelToComplex(400, 300)  →  [-0.75, 0.0]   (center of image)
     *
     * @param px  pixel column, 0 = left
     * @param py  pixel row,    0 = top
     * @return    a double[] of length 2:
     *              result[0] = cReal
     *              result[1] = cImag
     */
    public static double[] pixelToComplex(int px, int py) {
        // TODO: Calculate cReal using the formula above
        double cReal = X_MIN + ((double) px / IMAGE_WIDTH) * (X_MAX - X_MIN); // replace this

        // TODO: Calculate cImag using the formula above (remember to flip y!)
        double cImag = Y_MAX - ((double) py/IMAGE_HEIGHT) * (Y_MAX - Y_MIN); // replace this

        return new double[]{cReal, cImag};
    }


    // =========================================================================
    // PART 5 — Choosing a color based on the iteration count
    // =========================================================================

    /**
     * Returns an RGB color (as a single int) based on how many iterations
     * a point took to escape.
     *
     * BACKGROUND:
     *   Java's BufferedImage uses one int per pixel to store color.
     *   The int is packed as:  0xRRGGBB
     *   where RR = red byte (0–255), GG = green byte, BB = blue byte.
     *
     *   You build that int like this:
     *     int color = (red << 16) | (green << 8) | blue;
     *
     * YOUR TASK:
     *   1. If  iterations == MAX_ITERATIONS  (point is IN the set):
     *        return black: 0x000000
     *
     *   2. Otherwise, map the iteration count to a color.
     *      Simple grayscale approach:
     *        int gray = (int)(255.0 * iterations / MAX_ITERATIONS);
     *        return (gray << 16) | (gray << 8) | gray;
     *
     *      For a more colorful look (encouraged!):
     *        int red   = (int)(255.0 * iterations / MAX_ITERATIONS);
     *        int green = (int)(128.0 * iterations / MAX_ITERATIONS);
     *        int blue  = 200;
     *        return (red << 16) | (green << 8) | blue;
     *
     *   Feel free to experiment — there is no single correct color scheme.
     *   The tests only check that in-set pixels return 0x000000.
     *
     * @param iterations  the value returned by countIterations() for this pixel
     * @return            an RGB color packed as a single int (0xRRGGBB)
     */
    public static int getColor(int iterations) {
        // TODO: Return black (0x000000) if iterations == MAX_ITERATIONS

        // TODO: Otherwise, compute and return a color of your choice.

        if (iterations == MAX_ITERATIONS) {
            return 0x000000;  // sets color to black
        }
        int red = (int)(255.0 * iterations / MAX_ITERATIONS); // pink/red tone
        int green = (int)(50.0 * iterations / MAX_ITERATIONS); // purplish tone
        int blue = (int)(250.0 * iterations / MAX_ITERATIONS); //blueeee
        return(red << 16) | (green << 8) | blue;


    }


    // =========================================================================
    // main() — DO NOT MODIFY
    // =========================================================================

    /**
     * Renders the Mandelbrot set by calling your five methods for every pixel,
     * then saves the result as "mandelbrot.png".
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Rendering Mandelbrot set (" + IMAGE_WIDTH + "x" + IMAGE_HEIGHT + ")...");

        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int py = 0; py < IMAGE_HEIGHT; py++) {
            for (int px = 0; px < IMAGE_WIDTH; px++) {
                double[] c = pixelToComplex(px, py);
                int iters  = countIterations(c[0], c[1]);
                image.setRGB(px, py, getColor(iters));
            }
        }

        ImageIO.write(image, "png", new File("mandelbrot.png"));
        System.out.println("Done! Open mandelbrot.png to see your result.");
    }
}
