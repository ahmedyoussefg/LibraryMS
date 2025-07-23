package org.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name="book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;
    private String title;
    private String author;
    private String genre;

    @Column(name = "available_copies")
    private int availableCopies;

    public Book() {}
    public Book(String title, String author, String genre, int availableCopies) {
        this.title = title;
        this.author=author;
        this.genre = genre;
        this.availableCopies = availableCopies;
    }

    public void decreaseCopies() {
        if (availableCopies > 0) {
            availableCopies--;
        } else {
            throw new IllegalStateException("No copies available to borrow.");
        }
    }
    public void increaseCopies() {
        availableCopies++;
    }

    @Override
    public String toString() {
        return String.format("[ID: %s] %s by %s (%s) - Available: %d",
            bookId, title, author, genre, availableCopies);
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(bookId);
    }
}
