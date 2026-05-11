package lab_8;
import java.util.ArrayList;

class Book {
    private String title;
    private String Author;
    private String isbn;
    private boolean isAvailable;

    Book(String title, String Author, String isbn) {
        this.title       = title;
        this.Author      = Author;
        this.isbn        = isbn;
        this.isAvailable = true;
    }

    String getDetails() {
        return title + " By: " + Author + " |ISBN: " + isbn + " | Available: " + isAvailable;
    }

    void checkOut() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println(title + " has been checked out.");
        } else {
            System.out.println(title + " is already checked out.");  // fixed message
        }
    }

    void returnBook() {
        isAvailable = true;
        System.out.println(title + " has been returned.");
    }

    String getTitle()  { return title; }
    String getAuthor() { return Author; }
    String getIsbn()   { return isbn; }

    boolean isAvailable() {
        return this.isAvailable;
    }
}

class Member {
    private String name;
    private String memberId;
    private ArrayList<Book> borrowedBooks;

    Member(String name, String memberId) {
        this.name          = name;
        this.memberId      = memberId;
        this.borrowedBooks = new ArrayList<>();
    }

    void borrowBook(Book b) {
        if (borrowedBooks.size() >= 3) {          // borrow limit check
            System.out.println(name + " has reached the borrow limit.");
            return;
        }
        if (b.isAvailable()) {
            b.checkOut();
            borrowedBooks.add(b);
        } else {
            System.out.println(b.getTitle() + " is not available.");
        }
    }

    void returnBook(Book b) {
        b.returnBook();
        borrowedBooks.remove(b);
    }

    void listBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println(name + " has no borrowed books.");
        } else {
            System.out.println(name + "'s borrowed books:");
            for (Book b : borrowedBooks) {
                System.out.println("  - " + b.getTitle());
            }
        }
    }
}

class Library {
    private ArrayList<Book> books;

    Library() {
        this.books = new ArrayList<>();
    }

    void addBook(Book b) {
        books.add(b);
        System.out.println(b.getTitle() + " added to library.");
    }

    Book findByTitle(String title) {
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                return b;
            }
        }
        System.out.println(title + " Not Found");
        return null;
    }

    void listAvailable() {
        System.out.println("\n-- Available Books --");
        for (Book b : books) {
            if (b.isAvailable()) {
                System.out.println(b.getDetails());
            }
        }
        System.out.println("--------------------");
    }
}

public class lab_8 {
    public static void main(String[] args) {

        // set up library and add books
        Library library = new Library();
        library.addBook(new Book("The Alchemist",            "Paulo Coelho",  "978-00000000000"));
        library.addBook(new Book("1984",                     "George Orwell", "978-00000000001"));
        library.addBook(new Book("Clean Code",               "Robert Martin", "978-00000000002"));
        library.addBook(new Book("The Pragmatic Programmer", "Hunt & Thomas", "978-00000000003"));

        // create members
        Member alice = new Member("Alice", "M001");
        Member bob   = new Member("Bob",   "M002");

        // borrow some books
        System.out.println("\n-- Borrow Demo --");
        Book b1 = library.findByTitle("The Alchemist");
        Book b2 = library.findByTitle("1984");
        Book b3 = library.findByTitle("Clean Code");
        Book b4 = library.findByTitle("The Pragmatic Programmer");

        alice.borrowBook(b1);  // success
        alice.borrowBook(b2);  // success
        alice.borrowBook(b3);  // success
        alice.borrowBook(b4);  // should hit borrow limit of 3

        // list what alice has
        System.out.println();
        alice.listBorrowedBooks();

        // try borrowing an already checked out book
        System.out.println("\n-- Already Checked Out Demo --");
        bob.borrowBook(b1);    // already taken by alice

        // return a book and borrow again
        System.out.println("\n-- Return Demo --");
        alice.returnBook(b1);
        bob.borrowBook(b1);    // now available

        // list available books in the library
        library.listAvailable();
    }
}