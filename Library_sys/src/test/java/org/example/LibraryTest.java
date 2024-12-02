package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private static Library library;
    private static Book book1;
    private static Book book2;
    private static User user1;
    private static User user2;

    @BeforeEach
    void setUp() {
        library = new Library();
        book1 = new Book("Java Programming", "Author A", "ISBN123");
        book2 = new Book("Data Structures", "Author B", "ISBN456");
        user1 = new User("Steve Harvey", "User001");
        user2 = new User("Jim Carrey", "User002");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addBookTest() {
        library.addBook(book1);
        library.addBook(book2);
        assertEquals(2, library.getTotalNumberOfBooks());
    }

    @Test
    void registerUserTest() {
        library.registerUser(user1);
        library.registerUser(user2);
        //assertEquals(2, library.getTotalNumberOfBooks()); no way to check users?
    }

    @Test
    void borrowBookTest() {
        library.addBook(book1);
        library.addBook(book2);
        library.registerUser(user1);
        library.registerUser(user2);


        library.borrowBook("User001", "ISBN123");
        assertNotNull(user1.getBorrowedBooks());
        assertFalse(book1.isAvailable());
        library.returnBook("User001", "ISBN123");
        var output = user1.getBorrowedBooks();
        assertTrue(output.isEmpty());
        assertTrue(book1.isAvailable());

        assertThrows(IllegalStateException.class,
                () -> {
                    library.borrowBook("User001", "ISBN321");
                });

        assertThrows(IllegalStateException.class,
                () -> {
                    library.borrowBook("User007", "ISBN123");
                });
    }

    @Test
    void returnBookTest() {
        library.addBook(book1);
        library.addBook(book2);
        library.registerUser(user1);
        library.registerUser(user2);


        library.borrowBook("User001", "ISBN123");
        assertNotNull(user1.getBorrowedBooks());
        assertFalse(book1.isAvailable());
        library.returnBook("User001", "ISBN123");
        var output = user1.getBorrowedBooks();
        assertTrue(output.isEmpty());
        assertTrue(book1.isAvailable());

        assertThrows(IllegalStateException.class,
                () -> {
                    library.returnBook("User001", "ISBN321");
                });

        assertThrows(IllegalStateException.class,
                () -> {
                    library.returnBook("User007", "ISBN123");
                });
    }

    @Test
    void displayAvailableBooksTest() {
        library.addBook(book1);
        library.addBook(book2);
        library.registerUser(user1);
        library.registerUser(user2);


        // make sure the system prints what we want it to
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent)); // Redirect System.out
        try {
            library.displayAvailableBooks();
            String expectedOutput = "Available books in the library:" + System.lineSeparator() + book1.getTitle() + " by " + book1.getAuthor() + System.lineSeparator() + book2.getTitle() + " by " + book2.getAuthor() + System.lineSeparator();
            assertEquals(expectedOutput, outContent.toString());

            library.borrowBook("User001", "ISBN123");
            library.displayAvailableBooks();
            expectedOutput = expectedOutput + "Available books in the library:" + System.lineSeparator() + book2.getTitle() + " by " + book2.getAuthor() + System.lineSeparator();
            assertEquals(expectedOutput, outContent.toString());
            library.returnBook("User001", "ISBN123");
        } finally {
            // Restore the original System.out
            System.setOut(originalOut);
        }
    }

    @Test
    void getAvailableBooksTest() {
        library.addBook(book1);
        library.addBook(book2);
        library.registerUser(user1);
        library.registerUser(user2);


        assertNotNull(library.getAvailableBooks());
        library.borrowBook("User001", "ISBN123");
        library.borrowBook("User002", "ISBN456");
        var output = library.getAvailableBooks();
        assertTrue(output.isEmpty());
        library.returnBook("User001", "ISBN123");
        library.returnBook("User002", "ISBN456");
    }

    @Test
    void getTotalNumberOfBooksTest() {
        library.addBook(book1);
        library.addBook(book2);
        library.registerUser(user1);
        library.registerUser(user2);


        assertEquals(2, library.getTotalNumberOfBooks());
        Book book3 = new Book("Software Testing", "Author C", "ISBN789");
        library.addBook(book3);
        assertEquals(3, library.getTotalNumberOfBooks());
    }

    @Test
    void getTotalBorrowedBooksTest() {
        library.addBook(book1);
        library.addBook(book2);
        library.registerUser(user1);
        library.registerUser(user2);


        library.borrowBook("User001", "ISBN123");
        assertEquals(1, library.getTotalBorrowedBooks());
        library.borrowBook("User002", "ISBN456");
        assertEquals(2, library.getTotalBorrowedBooks());
        library.returnBook("User001", "ISBN123");
        library.returnBook("User002", "ISBN456");
    }

    @Test
    @DisplayName("Test getAverageBooksPerUser")
    void averageBooksPerUserWithZeroUsers() {
        library.addBook(book1);
        library.addBook(book2);

        assertEquals(0.0, library.getAverageBooksPerUser());
    }

    @Test
    @DisplayName("Test getAverageBooksPerUser")
    void getAverageBooksPerUserTest() {
        library.addBook(book1);
        library.addBook(book2);
        library.registerUser(user1);
        library.registerUser(user2);

        library.borrowBook("User001", "ISBN123");
        assertEquals(0.5, library.getAverageBooksPerUser());

        library.borrowBook("User002", "ISBN456");
        assertEquals(1, library.getAverageBooksPerUser());

        library.returnBook("User001", "ISBN123");
        library.returnBook("User002", "ISBN456");
    }

    // can we test the private findBookByIsbn function?
}