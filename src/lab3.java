import java.util.Scanner;
public class lab3 {
    public static void main(String[] args) {

        // question 1
        Scanner input = new Scanner(System.in);

        int secretNumber = 7;
        int guess = 0;

        while(guess != secretNumber) {
            System.out.print("Guess Number: ");
            guess = input.nextInt();

            if (guess < secretNumber) {
                System.out.println("Too low");

            }
            else if (guess > secretNumber) {
                System.out.println("Too high");
            }
            else {
                System.out.println("Correct!");
            }
        }
      //  input.close();


        // Question 2

        //Scanner input = new Scanner(System.in);

        int number;
        int largest = Integer.MIN_VALUE;

        for (int i = 1; i <=5; i++) {
            System.out.print("Enter number: ");
            number = input.nextInt();

            if (number> largest) {
                largest = number;
            }
        }
        System.out.println("The largest number is: " + largest);
       // input.close();

       // Question 3

        String day1 = "Monday";
        String day2 = "Tuesday";
        String day3 = "Wendsday";
        String day4 = "Thursday";
        String day5 = "Friday";
        String day6 = "Saturday";
        String day7 = "Sunday";

        System.out.print("Enter a number (1-7): ");
        int choice = input.nextInt();

        switch(choice) {
            case 1:
                System.out.println(day1);
                break;

            case 2:
                System.out.println(day2);
                break;
            case 3:
                System.out.println(day3);
                break;
            case 4:
                System.out.println(day4);
                break;
            case 5:
                System.out.println(day5);
                break;
            case 6:
                System.out.println(day6);
                break;
            case 7:
                System.out.println(day7);
                break;
            default:
                System.out.println("Invalid input! Please enter a number between 1 and 7");
        }
        input.close();



    }

}
