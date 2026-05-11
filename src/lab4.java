import java.util.Scanner;
public class lab4 {
    public static void main(String[] args) {
        Scanner inputSource = new Scanner(System.in);

        // --- PROBLEM 1: CALCULATOR ---
        System.out.println("=== PART 1: CALCULATOR ===");
        boolean keepGoing = true;

        while (keepGoing) {
            System.out.println("Enter first value: ");
            double xVal = inputSource.nextDouble();

            System.out.println("Enter second value: ");
            double yVal = inputSource.nextDouble();

            System.out.println("Choose operation (+, -, *, /, ^): ");
            char symbol = inputSource.next().charAt(0);

            double output = 0;
            boolean isSuccessful = true;

            switch (symbol) {
                case '+': output = add(xVal, yVal); break;
                case '-': output = subtract(xVal, yVal); break;
                case '*': output = multiply(xVal, yVal); break;
                case '/':
                    if (yVal == 0) {
                        System.out.println("Error: Cannot divide by zero.");
                        isSuccessful = false;
                    } else {
                        output = divide(xVal, yVal);
                    }
                    break;
                case '^': output = power(xVal, (int)yVal); break;
                default:
                    System.out.println("Invalid operator selected.");
                    isSuccessful = false;
            }

            if (isSuccessful) System.out.println("Result: " + output);

            System.out.print("Do another math problem? (y/n): ");
            if (inputSource.next().toLowerCase().charAt(0) != 'y') {
                keepGoing = false;
            }
        }

        // --- PROBLEM 2: NUMBER ANALYZER ---
        System.out.println("\n=== PART 2: NUMBER ANALYZER ===");
        System.out.print("Enter an integer to analyze: ");
        int userInt = inputSource.nextInt();

        System.out.println("Is Even: " + isEven(userInt));
        System.out.println("Is Prime: " + isPrime(userInt));
        System.out.println("Factorial: " + getFactorial(userInt));
        System.out.println("Sum of Digits: " + sumOfDigits(userInt));
        System.out.println("Reversed Number: " + reverseNumber(userInt));

        inputSource.close();
    }

    // --- CALCULATOR METHODS ---
    static double add(double val1, double val2) { return val1 + val2; }
    static double subtract(double val1, double val2) { return val1 - val2; }
    static double multiply(double val1, double val2) { return val1 * val2; }
    static double divide(double val1, double val2) { return val1 / val2; }

    static double power(double baseNum, int pwr) {
        double total = 1;
        for (int i = 0; i < pwr; i++) {
            total *= baseNum;
        }
        return total;
    }

    // --- ANALYZER METHODS ---
    public static boolean isEven(int target) {
        return target % 2 == 0;
    }

    public static boolean isPrime(int target) {
        if (target <= 1) return false;
        for (int j = 2; j < target; j++) {
            if (target % j == 0) return false;
        }
        return true;
    }

    public static int getFactorial(int target) {
        int resultVal = 1;
        for (int k = 1; k <= target; k++) {
            resultVal *= k;
        }
        return resultVal;
    }

    public static int sumOfDigits(int target) {
        int totalSum = 0;
        int remaining = Math.abs(target);
        while (remaining > 0) {
            totalSum += remaining % 10;
            remaining /= 10;
        }
        return totalSum;
    }

    public static int reverseNumber(int target) {
        int flipped = 0;
        while (target != 0) {
            int lastDigit = target % 10;
            flipped = flipped * 10 + lastDigit;
            target /= 10;
        }
        return flipped;
    }
}


