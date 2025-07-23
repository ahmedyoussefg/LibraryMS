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

public class LibrarySystem {
    private final Scanner scanner;
    private final BookDAO bookDAO;
    private final UserDAO userDAO;
    private final SearchService<Book> bookSearchService;

    private User currentUser = null;

    public LibrarySystem(Scanner scanner, BookDAO bookDAO, UserDAO userDAO,
                         SearchService<Book> bookSearchService) {
        this.scanner = scanner;
        this.bookDAO = bookDAO;
        this.userDAO = userDAO;
        this.bookSearchService = bookSearchService;
    }

    public void run() {
        System.out.println("📚 Welcome to the Library Management System 📚");
        while (true) {
            if (currentUser == null) {
                System.out.println("\n1. Sign Up\n2. Log In\n3. Exit");
                System.out.print("Choose option: ");
                String choice = scanner.nextLine();

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
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        User registrant = userDAO.get(username);
        if (registrant != null) {
            System.out.println("Username already exists.");
            return;
        }

        System.out.print("Sign up as Admin? (y/n): ");
        boolean isAdmin = scanner.nextLine().equalsIgnoreCase("y");

        User user = isAdmin ? new Admin(username) : new RegularUser(username);
        userDAO.save(user);
        System.out.println("Sign-up successful!");
    }

    private void logIn() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

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
                    "4. Register a New User\n5. Log Out");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addBook();
                case "2" -> deleteBook();
                case "3" -> viewCatalogue();
                case "4" -> registerUser();
                case "5" -> {
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
            System.out.println("1. View Catalogue\n2. Borrow Book\n3. Return Book\n4. My Borrowed Books\n5. Log Out");
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> viewCatalogue();
                case "2" -> borrowBook();
                case "3" -> returnBook();
                case "4" -> viewBorrowedBooks();
                case "5" -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void deleteBook() {
        viewCatalogue();
        System.out.print("Enter book ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
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
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Number of Available Copies: ");
        int availableCopies = Integer.parseInt(scanner.nextLine());

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
        System.out.print("Enter book ID to return: ");
        int bookId = Integer.parseInt(scanner.nextLine());
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
        System.out.print("Enter book ID to borrow: ");
        int bookId = Integer.parseInt(scanner.nextLine());
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
        System.out.print("Enter username for new user: ");
        String username = scanner.nextLine();
        User existingUser = userDAO.get(username);
        if (existingUser != null) {
            System.out.println("Username already exists.");
            return;
        }

        // check new user type
        System.out.print("Register the new user as Admin? (y/n): ");
        boolean isAdmin = scanner.nextLine().equalsIgnoreCase("y");

        User newUser = isAdmin ? new Admin(username) : new RegularUser(username);
        userDAO.save(newUser);
        System.out.println("New user registered successfully.");
    }
}

