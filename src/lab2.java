import java.util.Scanner;
public class lab2 {
    public static void main(String[] args) {
        // Part 1
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your full name: ");
        String name = input.nextLine();

        // removes leading and trailing spaces
        name = name.trim();

        // converts to uppercase
        String upperName = name.toUpperCase();

        // extracts first name
        String firstName = name.split(" ")[0];

        int characterCount = name.replace(" ", " ").length();

        System.out.println("Name: " + upperName);
        System.out.println("First Name: " + firstName);
        System.out.println("Character Count: " + characterCount);


// Part 2
//

        //Scanner input2 = new Scanner(System.in);
        System.out.println("Enter your Account Number: ");
        String accNum = input.nextLine();

        boolean isValid = true;
//        if AccNum > 8
        if (accNum.length() != 8) {
            isValid = false;
        }

        // check starts with 1
        if (accNum.charAt(0) != '1') {
            isValid = false;
        }

        // check all digits
        for (int i = 0; i < accNum.length(); i++) {
            if (!Character.isDigit(accNum.charAt(i))) {
                isValid = false;
                break;
            }
        }
        if (!isValid) {
            System.out.println("Invalid Account number");
            return;
        }

        // Part 3

        System.out.println("Enter initial balance: ");
        double balance = input.nextDouble();


        System.out.println("Enter deposit amount: ");
        double deposit = input.nextDouble();
        balance += deposit;

        System.out.println("Enter withdrawl amount: ");
        double withdrawal = input.nextDouble();
        balance -= withdrawal;

        input.nextLine();


        // Part 4 Pin
        System.out.println("Create a 4-digit PIN: ");
        String pin = input.nextLine();

        boolean validPin = true;

        if (pin.length() != 4) {
            validPin = false;
        }

        for (int i = 0; i < pin.length(); i++) {
            if (!Character.isDigit(pin.charAt(i))) {
                validPin = false;
                break;
            }
        }

        if (!validPin) {
            System.out.println("Invalid PIN");
            return;
        }

        System.out.println("Re-enter PIN: ");
        String loginPin = input.nextLine();

        if (pin.equals(loginPin)) {
            System.out.println("Login Successful");
        } else {
            System.out.println("Incorrect PIN");
            return;
        }

        // Part 5 Summary

        System.out.println("\nACCOUNT SUMMARY");
        System.out.println("Name: " + upperName);

        String masked = accNum.charAt(0) + "XXXXXXX";
        System.out.println("Account Number: " + masked);

        System.out.printf("Final Balance: $%.2f%n", balance);








    }
    }