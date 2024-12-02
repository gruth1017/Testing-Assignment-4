package org.example;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private static Book book1;
    private static Book book2;

    @BeforeAll
    static void setUp() {
        book1 = new Book("Java Programming", "Author A", "ISBN123");
        book2 = new Book("Data Structures", "Author B", "ISBN456");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getTitleTest() {
        assertEquals("Java Programming", book1.getTitle());
        assertEquals("Data Structures", book2.getTitle());
    }

    @Test
    void getAuthorTest() {
        assertEquals("Author A", book1.getAuthor());
        assertEquals("Author B", book2.getAuthor());
    }

    @Test
    void getIsbnTest() {
        assertEquals("ISBN123", book1.getIsbn());
        assertEquals("ISBN456", book2.getIsbn());
    }

    @Test
    @DisplayName("Test isAvailable")
    void bookIsNotAvailableAfterBorrowed() {
        book1.borrowBook();
        assertFalse(book1.isAvailable());  // book is not available after borrowed
        book1.returnBook();
    }

    @Test
    @DisplayName("Test isAvailable")
    void bookIsAvailableAfterReturning() {
        book1.returnBook();
        assertTrue(book1.isAvailable());  // book is available after returning
    }

    @Test
    @DisplayName("Test borrowBook")
    void borrowBookTest() {
        book1.borrowBook();
        assertFalse(book1.isAvailable());  // book is not available after borrowed

        assertThrows(IllegalStateException.class,
                () -> {
                    book1.borrowBook();  // book cannot be borrowed when not available
                });
        book1.returnBook();
    }

    @Test
    void returnBookTest() {
        book1.borrowBook();
        book1.returnBook();
        assertTrue(book1.isAvailable());
    }
}