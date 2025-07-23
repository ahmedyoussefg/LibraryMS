package org;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.dao.BookDAO;
import org.dao.UserDAO;
import org.model.Book;
import org.service.SearchService;

public class Main {
    public static void main(String[] args) {
         final BookDAO bookDAO = new BookDAO();
         final UserDAO userDAO = new UserDAO();
         final SearchService<Book> bookSearchService = new SearchService<>();

        LibrarySystem librarySystem = new LibrarySystem(bookDAO,
                userDAO, bookSearchService);
        librarySystem.run();
    }
}