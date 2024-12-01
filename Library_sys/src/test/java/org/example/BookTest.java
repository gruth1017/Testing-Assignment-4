package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book("Java Programming", "Author A", "ISBN123");
        book2 = new Book("Data Structures", "Author B", "ISBN456");
        //Book book3 = new Book("Software Testing", "Author C", "ISBN789");
        //Book book4 = new Book("Auto Testing", "Author D", "ISBN321");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getTitleTest() {
        //Book book1 = new Book("Java Programming", "Author A", "ISBN123");
        //Book book2 = new Book("Data Structures", "Author B", "ISBN456");
        assertEquals("Java Programming", book1.getTitle());
        assertEquals("Data Structures", book2.getTitle());
    }

    @Test
    void getAuthorTest() {
        //Book book1 = new Book("Java Programming", "Author A", "ISBN123");
        //Book book2 = new Book("Data Structures", "Author B", "ISBN456");
        assertEquals("Author A", book1.getAuthor());
        assertEquals("Author B", book2.getAuthor());
    }

    @Test
    void getIsbnTest() {
        //Book book1 = new Book("Java Programming", "Author A", "ISBN123");
        //Book book2 = new Book("Data Structures", "Author B", "ISBN456");
        assertEquals("ISBN123", book1.getIsbn());
        assertEquals("ISBN456", book2.getIsbn());
    }

    @Test
    void isAvailableTest() {
        assertTrue(book1.isAvailable());
        book1.borrowBook();
        assertFalse(book1.isAvailable());
        book1.returnBook();
        assertTrue(book1.isAvailable());
    }

    @Test
    void borrowBookTest() {
        book1.borrowBook();
        assertFalse(book1.isAvailable());
        assertThrows(IllegalStateException.class,
                () -> {
                    book1.borrowBook();
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