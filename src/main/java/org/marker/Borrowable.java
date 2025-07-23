package org.marker;

import org.model.Book;

public interface Borrowable {
    void borrowBook(Book book);
    void returnBook(Book book);
}
