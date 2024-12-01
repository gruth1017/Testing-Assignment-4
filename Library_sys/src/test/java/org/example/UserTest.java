package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user1;
    private User user2;
    private Book book1;

    @BeforeEach
    void setUp() {
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
        user1.borrowBook(book1);
        assertNotNull(user1.getBorrowedBooks());
        user1.returnBook(book1);
        var output = user1.getBorrowedBooks();
        assertTrue(output.isEmpty());
        assertThrows(IllegalStateException.class,
                () -> {
                    user1.returnBook(book1);;
                });

        user2.borrowBook(book1);
        assertNotNull(user2.getBorrowedBooks());
        user2.returnBook(book1);
        output = user2.getBorrowedBooks();
        assertTrue(output.isEmpty());
    }
}