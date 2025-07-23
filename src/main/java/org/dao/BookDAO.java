package org.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.model.Book;
import org.model.RegularUser;
import org.util.HibernateUtil;

public class BookDAO {
    public void save(Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(book);
            tx.commit();
        }
    }

    public Book get(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Book.class, id);
        }
    }

    public List<Book> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Book", Book.class).getResultList();
        }
    }

    public void update(Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(book);
            tx.commit();
        }
    }

    public void delete(Book book) {
         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            // Remove references in USERS_BOOKS table
            List<RegularUser> users = session.createQuery(
                "SELECT r FROM RegularUser r WHERE :book MEMBER OF r.borrowedBooks", RegularUser.class)
                .setParameter("book", book)
                .getResultList();

            for (RegularUser user : users) {
                user.returnBook(book);
                session.merge(user);
            }
            // remove the book
            session.remove(book);
            tx.commit();
        }
    }
}
