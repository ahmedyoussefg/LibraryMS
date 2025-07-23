package org;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.dao.BookDAO;
import org.dao.UserDAO;
import org.model.Book;
import org.service.SearchService;

public class Main {
    public static void main(String[] args) {
         final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
         final BookDAO bookDAO = new BookDAO();
         final UserDAO userDAO = new UserDAO();
         final SearchService<Book> bookSearchService = new SearchService<>();

        LibrarySystem librarySystem = new LibrarySystem(scanner, bookDAO,
                userDAO, bookSearchService);
        librarySystem.run();
        scanner.close();
    }
}