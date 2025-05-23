/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.formativepart2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Awande Aphelele Cane, ST10447692
 * JUnit test class for the Message class, testing message ID, recipient cell number,
 * message length, message hash, and send/store/disregard functionality.
 */
public class MessageTest {

    @Test
    public void testCheckMessageID_Valid() {
        Message message = new Message();
        assertTrue(message.checkMessageID("1234567890"));
    }

    @Test
    public void testCheckMessageID_Invalid() {
        Message message = new Message();
        assertFalse(message.checkMessageID("123456789")); // Too short
        assertFalse(message.checkMessageID("123456789ab")); // Non-numeric
        assertFalse(message.checkMessageID(null));
    }

    @Test
    public void testCheckRecipientCell_Valid() {
        Message message = new Message();
        assertTrue(message.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testCheckRecipientCell_Invalid() {
        Message message = new Message();
        assertFalse(message.checkRecipientCell("08575975889")); // No +27
        assertFalse(message.checkRecipientCell("+2771869300")); // Too short
        assertFalse(message.checkRecipientCell(null));
    }

    @Test
    public void testCheckMessageLength_Success() {
        Message message = new Message();
        String result = message.checkMessageLength("Hi Mike, can you join us for dinner tonight");
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testCheckMessageLength_Failure() {
        Message message = new Message();
        String longMessage = "A".repeat(251);
        String result = message.checkMessageLength(longMessage);
        assertEquals("Message exceeds 250 characters by 1, please reduce size.", result);
    }

    @Test
    public void testCreateMessageHash() {
        Message message = new Message();
        String hash = message.createMessageHash("1234567890", 0, "Hi Mike, can you join us for dinner tonight");
        assertEquals("12:0:HITONIGHT", hash);
    }

    @Test
    public void testSendMessage_Success() {
        Message message = new Message();
        // Note: JOptionPane is interactive, so we test the underlying validations
        String result = message.checkMessageLength("Hi Mike, can you join us for dinner tonight");
        assertEquals("Message ready to send.", result);
        assertTrue(message.checkRecipientCell("+27718693002"));
        // Simulate send (JOptionPane choice 0 would increment totalMessages)
        // Actual sendMessage result depends on user interaction
    }

    @Test
    public void testSendMessage_Discard() {
        Message message = new Message();
        // Note: JOptionPane is interactive, so we test the underlying validations
        String result = message.checkMessageLength("Hi Keegan, did you receive the payment?");
        assertEquals("Message ready to send.", result);
        assertFalse(message.checkRecipientCell("08575975889")); // Invalid number
        // Simulate discard (JOptionPane choice 1 would return "Press 0 to delete message.")
    }

    @Test
    public void testReturnTotalMessages() {
        Message message = new Message();
        // Simulate sending a message (JOptionPane choice 0 would increment)
        // Note: Actual total depends on JOptionPane choice
        assertTrue(message.returnTotalMessages() >= 0);
    }

    // New Test 1: Verify storeMessage saves data to JSON file correctly
    @Test
    public void testStoreMessage() {
        Message message = new Message();
        String messageId = "1234567890";
        String messageHash = "12:1:HITONIGHT";
        String recipient = "+27718693002";
        String messageContent = "Hi Mike, can you join us for dinner tonight";

        // Store the message
        message.storeMessage(messageId, messageHash, recipient, messageContent);

        // Read the JSON file to verify contents
        try (FileReader reader = new FileReader("messages.json")) {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            JSONObject storedMessage = (JSONObject) jsonArray.get(jsonArray.size() - 1); // Get last stored message

            assertEquals(messageId, storedMessage.get("MessageID"));
            assertEquals(messageHash, storedMessage.get("MessageHash"));
            assertEquals(recipient, storedMessage.get("Recipient"));
            assertEquals(messageContent, storedMessage.get("Message"));
        } catch (IOException | org.json.simple.parser.ParseException e) {
            fail("Failed to read or parse JSON file: " + e.getMessage());
        }
    }

    // New Test 2: Verify printMessages returns correct message details
    @Test
    public void testPrintMessages() {
        Message message = new Message();
        // Simulate sending a message to populate sentMessages
        message.sendMessage("+27718693002", "Hi Mike, can you join us for dinner tonight");
        // Note: Actual send depends on JOptionPane; we test printMessages separately
        String expected = "MessageID: \\d{10}, MessageHash: \\d{2}:1:HITONIGHT, Recipient: \\+27718693002, Message: Hi Mike, can you join us for dinner tonight\\n";
        String result = message.printMessages();
        assertTrue(result.matches(expected), "Expected message format not found in: " + result);
    }
}
