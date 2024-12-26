package com.tpe.controller;

import com.tpe.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class ProjectSystem {

    public static void main(String[] args) {

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        try (Session session = factory.openSession(); Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n=== User Management System ===");
                System.out.println("1. Create User");
                System.out.println("2. View All User");
                System.out.println("3. Update User");
                System.out.println("4. Delete User");
                System.out.println("0. Exit");
                System.out.println("Enter your choice");


                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {

                    case 1:
                        System.out.println("Enter username : ");
                        String username = scanner.nextLine();
                        System.out.println("Enter email : ");
                        String email = scanner.nextLine();
                        System.out.println("Enter password : ");
                        String password = scanner.nextLine();

                        User newUser = new User(username, email, password);
                        session.beginTransaction();
                        session.save(newUser);
                        session.getTransaction().commit();
                        System.out.println("User created successfully : " + newUser);

                        break;

                    case 2:
                        session.beginTransaction();
                        var users = session.createQuery("FROM User", User.class).list();
                        session.getTransaction().commit();
                        System.out.println("\nUsers : ");
                        for (User user : users) {
                            System.out.println(user);
                        }

                        break;

                    case 3:
                        System.out.println("Enter user ID to update : ");
                        Long userId = scanner.nextLong();
                        scanner.nextLine();

                        session.beginTransaction();
                        User userToUpdate = session.get(User.class, userId);

                        if (userToUpdate != null) {
                            System.out.println("Enter new username : ");
                            userToUpdate.setUsername(scanner.nextLine());
                            System.out.println("Enter new email : ");
                            userToUpdate.setEmail(scanner.nextLine());
                            System.out.println("Enter new password : ");
                            userToUpdate.setPassword(scanner.nextLine());

                            session.update(userToUpdate);
                            session.getTransaction().commit();
                            System.out.println("User updated successfully : " + userToUpdate);

                        } else {
                            session.getTransaction().rollback();
                            System.out.println("User not found.");
                        }
                        break;

                    case 4:
                        System.out.println("Enter user ID to delete : ");
                        Long deleteUserId = scanner.nextLong();

                        session.beginTransaction();
                        User userToDelete = session.get(User.class, deleteUserId);

                        if (userToDelete != null) {
                            session.delete(userToDelete);
                            session.getTransaction().commit();
                            System.out.println("User deleted successfully... ");
                        } else {
                            session.getTransaction().rollback();
                            System.out.println("User not found...");
                        }
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");

                }

            }

        } finally {
            factory.close();
        }

    }

}
