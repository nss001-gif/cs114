class Book {
    private String title;
    private String author;
    private int totalPages;
    private int currentPage;

    public Book (String title, String author, int totalPages) {
        this.title = title;
        this.author = author;
        this.totalPages = totalPages;
        this.currentPage = 0;
    }

    public void pageReader(int pages) {
        if (currentPage + pages > totalPages) {
            currentPage = totalPages;
            System.out.println("Finished! You reached the end of '" + title + "'.");
        } else {
            currentPage += pages;
            System.out.println("Read " + pages + " pages of '" + title + "'. Progress: " + currentPage + "/" + totalPages);
        }
    }

    public double getProgress() {
        return ((double) currentPage / totalPages) * 100.0;
    }

    public boolean isFinished() {
        return currentPage == totalPages;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalPages() { return totalPages; }
    public int getCurrentPage() { return currentPage; }

    @Override
    public String toString() {
        return "'" + title + "' by " + author + " (" + totalPages + " pages)";
    }
}

// --- TASK 2: MOVIE CLASS ---
class Movie {
    private String title;
    private int[] ratings;
    private int ratingCount;

    public Movie(String title) {
        this.title = title;
        this.ratings = new int[10];
        this.ratingCount = 0;
    }

    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            System.out.println("Invalid rating: " + rating + ". Must be between 1-5.");
            return;
        }
        if (ratingCount >= ratings.length) {
            System.out.println("Rating limit reached for " + title);
            return;
        }
        ratings[ratingCount] = rating;
        ratingCount++;
    }

    public double getAverageRating() {
        if (ratingCount == 0) return 0.0;
        int sum = 0;
        for (int i = 0; i < ratingCount; i++) {
            sum += ratings[i];
        }
        return (double) sum / ratingCount;
    }

    public int getHighestRating() {
        if (ratingCount == 0) return 0;
        int highest = ratings[0];
        for (int i = 1; i < ratingCount; i++) {
            if (ratings[i] > highest) {
                highest = ratings[i];
            }
        }
        return highest;
    }

    public String getTitle() { return title; }
}

// --- MAIN CLASS: Lab7 ---
public class lab7 {
    public static void main(String[] args) {

        // --- Testing Task 1: Library System ---
        System.out.println("--- Task 1: Library System ---");
        Book bookA = new Book("The Great Gatsby", "F. Scott Fitzgerald", 180);
        Book bookB = new Book("Their Eyes Were Watching God", "Zora Neale Hurston", 286);

        System.out.println("Testing " + bookA);
        bookA.pageReader(50);
        System.out.println("Progress: " + bookA.getProgress() + "%");
        bookA.pageReader(140); // Test overshoot
        System.out.println("Is Finished: " + bookA.isFinished());

        System.out.println("\nTesting " + bookB);
        bookB.pageReader(30);
        System.out.println("Progress: " + bookB.getProgress() + "%");

        // --- Testing Task 2: Movie Tracker ---
        System.out.println("\n--- Task 2: Movie Rating Tracker ---");
        Movie movieA = new Movie("Inception");
        Movie movieB = new Movie("The Matrix");

        movieA.addRating(5);
        movieA.addRating(4);
        movieA.addRating(9); // Test invalid

        movieB.addRating(3);
        movieB.addRating(5);
        movieB.addRating(0); // Test invalid

        System.out.println("\nStats for: " + movieA.getTitle());
        System.out.println("Average Rating: " + movieA.getAverageRating());
        System.out.println("Highest Rating: " + movieA.getHighestRating());

        System.out.println("\nStats for: " + movieB.getTitle());
        System.out.println("Average Rating: " + movieB.getAverageRating());
        System.out.println("Highest Rating: " + movieB.getHighestRating());
    }
}

