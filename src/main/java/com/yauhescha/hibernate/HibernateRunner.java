package com.yauhescha.hibernate;


import com.yauhescha.hibernate.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        // if nothing in hibernate.cfg.xml
        // configuration.addAnnotatedClass(User.class);

        try (SessionFactory factory = configuration.buildSessionFactory();
             Session session = factory.openSession();) {
            session.beginTransaction();

            User user = User.builder()
                    .username("ivan@gmail1.com")
                    .firstname("Ivan")
                    .lastname("Push")
                    .birthDate(LocalDate.of(2000, 1, 30))
                    .age(20)
                    .build();
            session.save(user);
            session.getTransaction().commit();

        }

    }
}
