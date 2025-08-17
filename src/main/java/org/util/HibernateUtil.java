package org.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.model.Admin;
import org.model.Book;
import org.model.RegularUser;
import org.model.User;

import io.github.cdimascio.dotenv.Dotenv;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
        System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        Configuration cfg = new Configuration().configure()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Admin.class)
                    .addAnnotatedClass(RegularUser.class)
                    .addAnnotatedClass(Book.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties());
        sessionFactory = cfg.buildSessionFactory(builder.build());
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}