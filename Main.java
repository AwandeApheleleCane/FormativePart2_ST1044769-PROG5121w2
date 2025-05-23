/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.formativepart2;

import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * @author Awande Aphelele Cane, ST10447692
 * The Main class integrates the Login and Message classes, providing a menu-driven interface
 * for user registration, login, and messaging functionality in the QuickChat application.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        Message message = new Message();
        boolean loggedIn = false;

        // Registration
        System.out.println("=== Register ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter cell phone number (+27xxxxxxxxx): ");
        String cellPhone = scanner.nextLine();
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.println(login.registerUser(username, password, cellPhone, firstName, lastName));

        // Login
        System.out.println("\n=== Login ===");
        System.out.print("Enter username: ");
        username = scanner.nextLine();
        System.out.print("Enter password: ");
        password = scanner.nextLine();

        String loginStatus = login.returnLoginStatus(username, password);
        System.out.println(loginStatus);
        loggedIn = login.loginUser(username, password);

        if (!loggedIn) {
            JOptionPane.showMessageDialog(null, "You must log in to send messages.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            scanner.close();
            return;
        }

        // Welcome message
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.", "QuickChat", JOptionPane.INFORMATION_MESSAGE);

        // Set number of messages
        System.out.print("How many messages would you like to send? ");
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next();
        }
        int maxMessages = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        int messagesEntered = 0;

        // Main menu loop
        while (true) {
            System.out.println("\n=== QuickChat Menu ===");
            System.out.println("1) Send Messages");
            System.out.println("2) Show Recently Sent Messages");
            System.out.println("3) Quit");
            System.out.print("Enter your choice (1-3): ");

            int choice;
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    if (messagesEntered >= maxMessages) {
                        JOptionPane.showMessageDialog(null, "Message limit reached.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    System.out.print("Enter recipient number (+27xxxxxxxxx): ");
                    String recipient = scanner.nextLine();
                    System.out.print("Enter message (≤250 characters): ");
                    String messageContent = scanner.nextLine();
                    String result = message.sendMessage(recipient, messageContent);
                    JOptionPane.showMessageDialog(null, result, "Message Status", JOptionPane.INFORMATION_MESSAGE);
                    if (result.equals("Message successfully sent.")) {
                        messagesEntered++;
                    }
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Coming Soon.", "Feature Not Available", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Total messages sent: " + message.returnTotalMessages(), "Summary", JOptionPane.INFORMATION_MESSAGE);
                    scanner.close();
                    System.exit(0);
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please select 1-3.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
