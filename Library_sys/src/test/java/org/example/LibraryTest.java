package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    public static Library library;
    private static Book book1;
    private static Book book2;
    private static User user1;
    private static User user2;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        library = new Library();
        book1 = new Book("Java Programming", "Author A", "ISBN123");
        book2 = new Book("Data Structures", "Author B", "ISBN456");
        user1 = new User("Steve Harvey", "User001");
        user2 = new User("Jim Carrey", "User002");

        // Redirect System.out to capture output for display tests
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent)); // Redirect System.out
    }

    // Method resets the system after each test for a clean slate
    @AfterEach
    void tearDown() {
        // Restore the original System.out after each test
        System.setOut(originalOut);
        // Reset captured output buffer
        outContent.reset();
    }

    // Test adds books to the Library and checks the total # of books
    @Test
    void addBookTest() {
        addBooksToLibrary(); // adds books before testing
        assertEquals(2, library.getTotalNumberOfBooks());
    }

    // test for registering users in the library
    @Test
    void registerUserTest() {
       registerUsers(); // Registers users before testing
        //assertEquals(2, library.getTotalNumberOfBooks()); no way to check users?
    }

    // test borrowing and returning a book
    @Test
    void borrowBookTest() {
        setupBooksAndUsers(); // Setup books and users
        borrowAndReturnBook(user1, book1); // test borrowing and returning for user1
        testBorrowBookFailures(); // test failure scenarios like invalid borrow
    }

    // test returning a book
    @Test
    void returnBookTest() {
        setupBooksAndUsers(); // setup books and users
        borrowAndReturnBook(user1, book1); // borrow and return a book
        testInvalidReturn(user1, book1); // tests invalid return scenarios
    }

    // test display available books in the library
    @Test
    void displayAvailableBooksTest() {
        setupBooksAndUsers();
        displayBooksWithBorrowing(); // test display functionality after borrowing
    }

    // test getting avilable books list
    @Test
    void getAvailableBooksTest() {
        setupBooksAndUsers();
        borrowBooksAndCheckAvailability(); // test avialbility after borrowing
    }

    // test total # of books in library
    @Test
    void getTotalNumberOfBooksTest() {
        setupBooksAndUsers();
        // add additional book
        addBookToLibrary(new Book("Software Testing", "Author C", "ISBN789"));
        // assert there are 3 books now
        assertEquals(3, library.getTotalNumberOfBooks());

    }

    // Test getting the total number of borrowed books
    @Test
    void getTotalBorrowedBooksTest() {
        setupBooksAndUsers();  // Setup books and users
        borrowBooksAndVerifyTotal();  // Verify total borrowed books
    }


    // Edge Case Tests
    @Test
    void testNoBooksInLibrary() {
        // When no books are added, check if the output is as expected
        assertDoesNotThrow(() -> library.displayAvailableBooks(), "No exception should be thrown when no books are added.");
    }

    @Test
    void testNoUsersRegistered() {
        // When no users are registered, trying to borrow a book should throw an exception
        addBooksToLibrary();  // Add books but no users
        assertThrows(IllegalStateException.class,
                () -> {
                    library.borrowBook("NonExistentUser", "ISBN123"); // No users registered
                },
                "Should throw exception when trying to borrow with no registered users."
        );
    }

    @Test
    void testInvalidISBNWhenBorrowing() {
        // Trying to borrow a book with an invalid ISBN should throw an exception
        addBooksToLibrary();  // Add books
        registerUsers();  // Register users
        assertThrows(IllegalStateException.class,
                () -> {
                    library.borrowBook("User001", "InvalidISBN"); // Invalid ISBN
                },
                "Should throw exception for invalid ISBN"
        );
    }


    /*
    @Test
    void testLargeNumberOfUsers() {
        // Simulating 1 million users and ensures they are handled correctly
        int userCount = 1000000; // Simulate 1 million users
        for (int i = 0; i < userCount; i++) {
            User user = new User("User" + i, "UserID" + i);
            library.registerUser(user); // Register user
            library.borrowBook("UserID" + i, book1.getIsbn()); // Every user borrows the book
        }
        // getTotalNumberOfUsers() keeps getting an error and I cant modify the Library.class to fix it
        assertEquals(userCount, library.getTotalNumberOfUsers(), "Total number of users should be 1 million.");
    }*/

    @Test
    void testLargeNumberOfBooks() {
        // Simulating 100,000 books and ensures they are handled correctly
        int bookCount = 100000; // Simulate 100,000 books
        for (int i = 0; i < bookCount; i++) {
            Book book = new Book("Book" + i, "Author" + i, "ISBN" + i);
            library.addBook(book);  // Add book
        }
        assertEquals(bookCount, library.getTotalNumberOfBooks(), "Total number of books should be 100,000.");
    }

    @Test
    void testBorrowingBookAlreadyBorrowed() {
        setupBooksAndUsers();  // Setup books and users
        library.borrowBook(user1.getUserId(), book1.getIsbn());
        assertThrows(IllegalStateException.class,
                () -> {
                    library.borrowBook(user2.getUserId(), book1.getIsbn()); // Trying to borrow the same book
                },
                "Should throw exception when trying to borrow a book that is already borrowed by another user."
        );
    }

    // Helper Methods
    private void addBooksToLibrary() {
        library.addBook(book1);
        library.addBook(book2);
    }

    private void registerUsers() {
        library.registerUser(user1);
        library.registerUser(user2);
    }

    private void setupBooksAndUsers() {
        addBooksToLibrary();
        registerUsers();
    }

    private void borrowAndReturnBook(User user, Book book) {
        library.borrowBook(user.getUserId(), book.getIsbn());
        assertNotNull(user.getBorrowedBooks());
        assertFalse(book.isAvailable());
        library.returnBook(user.getUserId(), book.getIsbn());
        assertTrue(user.getBorrowedBooks().isEmpty());
        assertTrue(book.isAvailable());
    }

    private void testInvalidReturn(User user, Book book) {
        assertThrows(IllegalStateException.class,
                () -> {
                    library.returnBook(user.getUserId(), book.getIsbn());
                });
    }

    private void testBorrowBookFailures() {
        assertThrows(IllegalStateException.class,
                () -> {
                    library.borrowBook("User001", "ISBN321");
                });
        assertThrows(IllegalStateException.class,
                () -> {
                    library.borrowBook("User007", "ISBN123");
                });
    }

    private void displayBooksWithBorrowing() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent)); // Redirect System.out

        library.displayAvailableBooks();
        String expectedOutput = "Available books in the library:" + System.lineSeparator() + book1.getTitle() + " by " + book1.getAuthor() + System.lineSeparator() + book2.getTitle() + " by " + book2.getAuthor() + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());

        library.borrowBook("User001", "ISBN123");
        library.displayAvailableBooks();
        expectedOutput = expectedOutput + "Available books in the library:" + System.lineSeparator() + book2.getTitle() + " by " + book2.getAuthor() + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());

        library.returnBook("User001", "ISBN123");

        // Restore the original System.out
        System.setOut(originalOut);
    }

    private void borrowBooksAndCheckAvailability() {
        library.borrowBook("User001", "ISBN123");
        library.borrowBook("User002", "ISBN456");
        var availableBooks = library.getAvailableBooks();
        assertTrue(availableBooks.isEmpty());
        library.returnBook("User001", "ISBN123");
        library.returnBook("User002", "ISBN456");
    }

    private void borrowBooksAndVerifyTotal() {
        library.borrowBook("User001", "ISBN123");
        assertEquals(1, library.getTotalBorrowedBooks());
        library.borrowBook("User002", "ISBN456");
        assertEquals(2, library.getTotalBorrowedBooks());
        library.returnBook("User001", "ISBN123");
        library.returnBook("User002", "ISBN456");
    }

    private void borrowBooksAndCheckAverage() {
        library.borrowBook("User001", "ISBN123");
        assertEquals(0.5, library.getAverageBooksPerUser());

        library.borrowBook("User002", "ISBN456");
        assertEquals(1, library.getAverageBooksPerUser());

        library.returnBook("User001", "ISBN123");
        library.returnBook("User002", "ISBN456");
    }

    private void addBookToLibrary(Book book) {
        library.addBook(book);
    }
    // can we test the private findBookByIsbn function?
}
