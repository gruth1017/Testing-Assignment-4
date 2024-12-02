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
        book1.returnBook(); // ensure books are returned after each test
        book2.returnBook();
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
    @DisplayName("Test isAvailable - Book Not Available After Borrowed")
    void bookIsNotAvailableAfterBorrowed() {
        book1.borrowBook();
        assertFalse(book1.isAvailable(), "Book should not be available after being borrowed");
    }

    @Test
    @DisplayName("Test isAvailable - Book Available After Returning")
    void bookIsAvailableAfterReturning() {
        book1.borrowBook();
        book1.returnBook();
        assertTrue(book1.isAvailable());  // book is available after returning
    }

    @Test
    @DisplayName("Test borrowBook - Borrowing When Available")
    void borrowBookTest() {
        book1.borrowBook();
        assertFalse(book1.isAvailable(),"Book should not be available after being borrowed");  // book is not available after borrowed

    }

    @Test
    @DisplayName("Test borrowBook - Borrowing When Not Available")
    void borrowBookWhenUnavailable() {
        book1.borrowBook();
        assertThrows(IllegalStateException.class,
                book1::borrowBook,
                "An exception should be thrown when borrowing an already borrowed book");
    }

    @Test
    @DisplayName("Test returnBook - Returning Borrowed Book")
    void returnBookTest() {
        book1.borrowBook();
        book1.returnBook();
        assertTrue(book1.isAvailable(), "Book should be available after being returned");
    }

    @Test
    @DisplayName("Test returnBook - Returning Already Available Book")
    void returnAlreadyAvailableBook() {
        assertThrows(IllegalStateException.class,
                book1::returnBook,
                "An exception should be thrown when returning a book that is not borrowed");
    }

    @Test
    @DisplayName("Test toString Method")
    void toStringTest() {
        String expected = "Book{title='Java Programming', author='Author A', isbn='ISBN123', available=true}";
        assertEquals(expected, book1.toString(), "toString method should return the expected output");
    }
}
