package org;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.dao.BookDAO;
import org.dao.UserDAO;
import org.marker.Borrowable;
import org.model.Admin;
import org.model.Book;
import org.model.RegularUser;
import org.model.User;
import org.service.SearchService;
import org.util.InputUtil;

public class LibrarySystem {
    private final BookDAO bookDAO;
    private final UserDAO userDAO;
    private final SearchService<Book> bookSearchService;

    private User currentUser = null;

    public LibrarySystem(BookDAO bookDAO, UserDAO userDAO,
                         SearchService<Book> bookSearchService) {
        this.bookDAO = bookDAO;
        this.userDAO = userDAO;
        this.bookSearchService = bookSearchService;
    }

    public void run() {
        System.out.println("📚 Welcome to the Library Management System 📚");
        while (true) {
            if (currentUser == null) {
                System.out.println("\n1. Sign Up\n2. Log In\n3. Exit");
                String choice = InputUtil.prompt("Choose option: ");

                switch (choice) {
                    case "1" -> signUp();
                    case "2" -> logIn();
                    case "3" -> {
                        // exit
                        System.out.println("Exitting the program...");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } else {
                if (currentUser instanceof Admin) {
                    adminMenu();
                } else {
                    userMenu();
                }
            }
        }
    }

    private void signUp() {
        String username = InputUtil.promptNonEmpty("Enter username: ");
        User registrant = userDAO.get(username);
        if (registrant != null) {
            System.out.println("Username already exists.");
            return;
        }

        boolean isAdmin = InputUtil.promptYesNo("Sign up as Admin?");

        User user = isAdmin ? new Admin(username) : new RegularUser(username);
        userDAO.save(user);
        System.out.println("Sign-up successful!");
    }

    private void logIn() {
        String username = InputUtil.promptNonEmpty("Enter username: ");

        User user = userDAO.get(username);
        if (user == null) { 
            System.out.println("User not found.");
        } else {
            currentUser = user;
            System.out.printf("Logged in as %s (%s)%n", user.getUsername(),
                    user instanceof Admin ? "Admin" : "Regular User");
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Book\n2. Delete Book\n3. View Catalogue\n" +
                    "4. Search Books\n5. Register a New User\n6. Log Out");
            String choice = InputUtil.prompt("Choose option: ");

            switch (choice) {
                case "1" -> addBook();
                case "2" -> deleteBook();
                case "3" -> viewCatalogue();
                case "4" -> searchBooks();
                case "5" -> registerUser();
                case "6" -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void userMenu() {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View Catalogue\n2. Search Books\n3. Borrow Book\n4. Return Book\n" +
                    "5. My Borrowed Books\n6. Log Out");
            String choice = InputUtil.prompt("Choose option: ");

            switch (choice) {
                case "1" -> viewCatalogue();
                case "2" -> searchBooks();
                case "3" -> borrowBook();
                case "4" -> returnBook();
                case "5" -> viewBorrowedBooks();
                case "6" -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void deleteBook() {
        viewCatalogue();
        int id = InputUtil.promptInt("Enter book ID to delete: ");
        Book book = bookDAO.get(id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        bookDAO.delete(book);
        System.out.printf("Book with ID %d deleted successfully.\n", id);
    }

    private void addBook() {
        viewCatalogue();
        System.out.println("Getting ready to add a new book...");
        String title = InputUtil.promptNonEmpty("Title: ");
        String author = InputUtil.promptNonEmpty("Author: ");
        String genre = InputUtil.promptNonEmpty("Genre: ");
        int availableCopies = InputUtil.promptInt("Number of Available Copies: ");

        if (availableCopies <= 0) {
            System.out.println("Available copies must be a positive number.");
            return;
        }

        bookDAO.save(new Book(title, author, genre, availableCopies));
        System.out.println("Book added successfully.");
    }

    private void viewBorrowedBooks() {
        List<Book> borrowed;
        if (currentUser instanceof RegularUser) {
            borrowed = ((RegularUser) currentUser).getBorrowedBooks();
        } else {
            System.out.println("You must be a REGULAR user to view borrowed books.");
            return;
        }
        System.out.println("\n--- Your Borrowed Books ---");
        if (borrowed.isEmpty()) {
            System.out.println("You have no borrowed books.");
        } else {
            System.out.println("Your Borrowed Books:");
            borrowed.stream()
                    .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                    .forEach(System.out::println);
        }
    }

    private void returnBook() {
        if (!(currentUser instanceof RegularUser)) {
            System.out.println("You must be a REGULAR user to return borrowed books.");
            return;
        }
        viewBorrowedBooks();
        int bookId = InputUtil.promptInt("Enter book ID to return: ");
        Book book = bookDAO.get(bookId);
        List<Book> borrowedBooks = ((RegularUser) currentUser).getBorrowedBooks();
        if (book == null || !borrowedBooks.contains(book)) {
            System.out.println("You didn’t borrow this book.");
            return;
        }

        ((Borrowable) currentUser).returnBook(book);
        book.increaseCopies();
        bookDAO.update(book);
        userDAO.update(currentUser);
        System.out.println("Book returned successfully.");
    }

    private void borrowBook() {
        if (!(currentUser instanceof RegularUser)) {
            System.out.println("You must be a REGULAR user to return borrowed books.");
            return;
        }
        viewCatalogue();
        viewBorrowedBooks();
        int bookId = InputUtil.promptInt("Enter book ID to borrow: ");
        Book book = bookDAO.get(bookId);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (book.getAvailableCopies() == 0) {
            System.out.println("No copies available.");
            return;
        }

        if (((RegularUser) currentUser).getBorrowedBooks().contains(book)) {
            System.out.println("You already borrowed this book.");
            return;
        }

        book.decreaseCopies();
        ((Borrowable) currentUser).borrowBook(book);
        bookDAO.update(book);
        userDAO.update(currentUser);
        System.out.println("Book borrowed successfully.");
    }

    private void viewCatalogue() {
        System.out.println("\n--- Book Catalogue ---");
        List<Book> books = bookDAO.getAll();
        if (books.isEmpty()) {
            System.out.println("No books in catalogue.");
        } else {
            System.out.println("\nAvailable Books:");
            books.stream()
                 .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                 .forEach(System.out::println);
        }
    }

    private void registerUser() {
        if (!(currentUser instanceof Admin)) {
            System.out.println("You must be an Admin to register a new user.");
            return;
        }
        String username = InputUtil.promptNonEmpty("Enter username for new user: ");
        User existingUser = userDAO.get(username);
        if (existingUser != null) {
            System.out.println("Username already exists.");
            return;
        }

        // check new user type
        boolean isAdmin = InputUtil.promptYesNo("Register the new user as Admin?");

        User newUser = isAdmin ? new Admin(username) : new RegularUser(username);
        userDAO.save(newUser);
        System.out.println("New user registered successfully.");
    }
    private void searchBooks() {
        System.out.println("\n--- Search Books ---");
        String option = InputUtil.prompt("Search by (1) ID or (2) Title? ");
        List<Book> latestBooks = bookDAO.getAll();
        List<Book> results;
        switch (option) {
            case "1" -> {
                int findId = InputUtil.promptInt("Enter Book ID: ");
                results = bookSearchService.searchById(latestBooks, findId);
            }
            case "2" -> {
                String findTitle = InputUtil.promptNonEmpty("Enter Book Title: ");
                results = bookSearchService.searchByTitle(latestBooks, findTitle);
            }
            default -> {
                System.out.println("Invalid search option.");
                return;
            }
        }

        if (results.isEmpty()) {
            System.out.println("No matching books found.");
        } else {
            System.out.println("\nSearch Results:");
            results.stream()
                   .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                   .forEach(System.out::println);
        }
    }
}

