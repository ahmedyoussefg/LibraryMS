package org.model;

import java.util.List;

import org.marker.Borrowable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("REGULAR")
public class RegularUser extends User implements Borrowable {
    @ManyToMany(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="USERS_BOOKS", joinColumns = @JoinColumn(name="user_id"),
                inverseJoinColumns = @JoinColumn(name="book_id"))
    private List<Book> borrowedBooks;
    public RegularUser() {}
    public RegularUser(String username) {
        super(username);
    }
    @Override
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    @Override
    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
}
