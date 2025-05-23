/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.formativepart2;

import java.util.Scanner;

/**
 * @author Awande Aphelele Cane, ST10447692
 * The Login class implements a user registration and login system in Java, validating usernames (underscore, ≤5 chars),
 * passwords (≥8 chars, capital, number, special char), and South African cell phone numbers (+27 followed by 9 digits).
 * It provides methods to register users, verify login credentials, and return status messages, with JUnit tests ensuring functionality meets specified requirements.
 */
public class Login {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String cellPhoneNumber;

    public Login() {
        this.username = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.cellPhoneNumber = "";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean checkUserName(String username) {
        if (username == null) return false;
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasCapital = false, hasNumber = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) return false;
        String regex = "^\\+27\\d{9}$";
        return cellPhoneNumber.matches(regex);
    }

    public String registerUser(String username, String password, String cellPhoneNumber, String firstName, String lastName) {
        boolean validUsername = checkUserName(username);
        boolean validPassword = checkPasswordComplexity(password);
        boolean validCellPhone = checkCellPhoneNumber(cellPhoneNumber);

        StringBuilder message = new StringBuilder();

        if (!validUsername) {
            message.append("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.\n");
        } else {
            message.append("Username successfully captured.\n");
            this.username = username;
        }

        if (!validPassword) {
            message.append("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.\n");
        } else {
            message.append("Password successfully captured.\n");
            this.password = password;
        }

        if (!validCellPhone) {
            message.append("Cell phone number incorrectly formatted or does not contain international code.\n");
        } else {
            message.append("Cell phone number successfully added.\n");
            this.cellPhoneNumber = cellPhoneNumber;
        }

        if (validUsername && validPassword && validCellPhone) {
            this.firstName = firstName;
            this.lastName = lastName;
            return "User successfully registered.\n" + message.toString();
        }

        return message.toString();
    }

    public boolean loginUser(String username, String password) {
        if (this.username == null || this.password == null || username == null || password == null) {
            return false;
        }
        return this.username.equals(username) && this.password.equals(password);
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
}
