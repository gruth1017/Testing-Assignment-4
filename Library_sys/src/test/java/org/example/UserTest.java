package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.example.LibraryTest.library;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static User user1;
    private static User user2;
    private static Book book1;

    @BeforeAll
    static void setUp() {
        user1 = new User("Steve Harvey", "User001");
        user2 = new User("Jim Carrey", "User002");
        book1 = new Book("Java Programming", "Author A", "ISBN123");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getNameTest() {
        assertEquals("Steve Harvey", user1.getName());
        assertEquals("Jim Carrey", user2.getName());
    }

    @Test
    void getUserIdTest() {
        assertEquals("User001", user1.getUserId());
        assertEquals("User002", user2.getUserId());
    }

    @Test
    void getBorrowedBooksTest() {
        user1.borrowBook(book1);
        assertNotNull(user1.getBorrowedBooks());
        user1.returnBook(book1);
    }

    @Test
    void borrowBookTest() {
        user1.borrowBook(book1);
        assertNotNull(user1.getBorrowedBooks());
        assertThrows(IllegalStateException.class,
                () -> {
                    user2.borrowBook(book1);
                });
        user1.returnBook(book1);
        user2.borrowBook(book1);
        assertNotNull(user2.getBorrowedBooks());
        user2.returnBook(book1);
    }

    @Test
    void returnBookTest() {
        setUpBorrowedBook(user1, book1); // Modular setup for borrowing
        testSuccessfulReturn(user1, book1); // Validate successful book return
        testInvalidReturn(user1, book1); // Check for invalid return cases

        setUpBorrowedBook(user2, book1); // Test same for another user
        testSuccessfulReturn(user2, book1);
    }

    // Helper method: Setup for borrowing a book
    private void setUpBorrowedBook(User user, Book book) {
        user.borrowBook(book);
        assertNotNull(user.getBorrowedBooks(), "Borrowed books list should not be null after borrowing.");
    }

    // Helper method: Test successful book return
    private void testSuccessfulReturn(User user, Book book) {
        user.returnBook(book);
        var output = user.getBorrowedBooks();
        assertTrue(output.isEmpty(), "Borrowed books list should be empty after returning all books.");
    }

    // Helper method: Test invalid return cases
    private void testInvalidReturn(User user, Book book) {
        assertThrows(IllegalStateException.class,
                () -> {
                    user.returnBook(book); // Attempt to return a book that's already returned
                });
    }

    // Testing with NO users registered
    @Test
    void testNoUsers() {
        assertThrows(IllegalStateException.class,
                () -> {
                    library.borrowBook("NonExistentUser", "ISBN123");
                }, "Should throw exception when trying to borrow a book with no registered users."
        );

        assertThrows(IllegalStateException.class,
                () -> {
                    library.returnBook("NonExistentUser", "ISBN123");
                }, "Should throw exception when trying to return a book with no registered users."
        );
    }

    // Test Empty or Null inputs
    @Test
    void testEmptyUserId() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    library.borrowBook("", "ISBN123"); // Invalid userId
                }, "Should throw exception for empty userId."
        );
    }

    @Test
    void testNullBook() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    user1.borrowBook(null); // Passing a null book
                }, "Should throw exception for null book."
        );
    }

    // test borrowing a book when someone else has it
    @Test
    void testBookAlreadyBorrowed() {
        setUpBorrowedBook(user1, book1); // User1 borrows the book

        // User2 should not be able to borrow the same book
        assertThrows(IllegalStateException.class,
                () -> {
                    user2.borrowBook(book1); // Book already borrowed by User1
                }, "Should throw exception when User2 tries to borrow a book already borrowed by User1."
        );
    }

    @Test
    void testEdgeCases() {
        testNoUsers();
        testEmptyUserId();
        testNullBook();
        testBookAlreadyBorrowed();
    }

}
