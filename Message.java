/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.formativepart2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Awande Aphelele Cane, ST10447692
 * The Message class manages message creation, validation, and storage for the QuickChat application.
 * It supports message ID generation, recipient validation, message hash creation, and JSON storage.
 */
public class Message {
    private String messageId;
    private int messageNumber;
    private String recipient;
    private String messageContent;
    private String messageHash;
    private static List<JSONObject> sentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    // Generates a random 10-digit message ID
    public boolean checkMessageID(String messageId) {
        if (messageId == null) return false;
        return messageId.matches("\\d{10}");
    }

    // Validates recipient cell number (≤10 digits, starts with +27)
    public boolean checkRecipientCell(String recipient) {
        if (recipient == null) return false;
        String regex = "^\\+27\\d{9}$";
        return recipient.matches(regex);
    }

    // Validates message length (≤250 characters)
    public String checkMessageLength(String message) {
        if (message == null) return "Message cannot be null.";
        if (message.length() > 250) {
            return "Message exceeds 250 characters by " + (message.length() - 250) + ", please reduce size.";
        }
        return "Message ready to send.";
    }

    // Creates message hash: first 2 digits of message ID, message number, first and last words
    public String createMessageHash(String messageId, int messageNumber, String messageContent) {
        if (messageId == null || messageContent == null || messageContent.trim().isEmpty()) {
            return "";
        }
        String[] words = messageContent.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        String firstTwoDigits = messageId.length() >= 2 ? messageId.substring(0, 2) : "00";
        return String.format("%s:%d:%s%s", firstTwoDigits, messageNumber, firstWord.toUpperCase(), lastWord.toUpperCase());
    }

    // Allows user to send, disregard, or store the message
    public String sendMessage(String recipient, String messageContent) {
        this.messageId = generateMessageId();
        this.messageNumber = totalMessagesSent + 1;
        this.recipient = recipient;
        this.messageContent = messageContent;
        this.messageHash = createMessageHash(messageId, messageNumber, messageContent);

        String lengthCheck = checkMessageLength(messageContent);
        if (!lengthCheck.equals("Message ready to send.")) {
            return lengthCheck;
        }
        if (!checkRecipientCell(recipient)) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        if (!checkMessageID(messageId)) {
            return "Invalid message ID.";
        }

        String[] options = {"Send Message", "Disregard Message", "Store Message"};
        int choice = JOptionPane.showOptionDialog(null, "Choose an action:", "Message Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0: // Send Message
                totalMessagesSent++;
                JSONObject messageObj = new JSONObject();
                messageObj.put("MessageID", messageId);
                messageObj.put("MessageHash", messageHash);
                messageObj.put("Recipient", recipient);
                messageObj.put("Message", messageContent);
                sentMessages.add(messageObj);
                displayMessage(messageObj);
                return "Message successfully sent.";
            case 1: // Disregard Message
                return "Press 0 to delete message.";
            case 2: // Store Message
                storeMessage(messageId, messageHash, recipient, messageContent);
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    // Generates a random 10-digit message ID
    private String generateMessageId() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    // Displays message details using JOptionPane
    private void displayMessage(JSONObject message) {
        String display = String.format("MessageID: %s\nMessageHash: %s\nRecipient: %s\nMessage: %s",
                message.get("MessageID"), message.get("MessageHash"), message.get("Recipient"), message.get("Message"));
        JOptionPane.showMessageDialog(null, display, "Message Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // Stores message in a JSON file
    public void storeMessage(String messageId, String messageHash, String recipient, String messageContent) {
        JSONObject messageObj = new JSONObject();
        messageObj.put("MessageID", messageId);
        messageObj.put("MessageHash", messageHash);
        messageObj.put("Recipient", recipient);
        messageObj.put("Message", messageContent);

        JSONArray messageArray = new JSONArray();
        messageArray.add(messageObj);

        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(messageArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Returns a string of all sent messages
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }
        StringBuilder messages = new StringBuilder();
        for (JSONObject msg : sentMessages) {
            messages.append(String.format("MessageID: %s, MessageHash: %s, Recipient: %s, Message: %s\n",
                    msg.get("MessageID"), msg.get("MessageHash"), msg.get("Recipient"), msg.get("Message")));
        }
        return messages.toString();
    }

    // Returns the total number of messages sent
    public int returnTotalMessages() {
        return totalMessagesSent;
    }
}
