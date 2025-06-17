package quickchatmanagerapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.*;

public class QuickChatManagerApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Registration/Login Components
    private JTextField usernameField, phoneField;
    private JPasswordField passwordField;
    private JButton signUpButton, loginButton;

    // Message Manager Components
    private JTextArea reportArea;
    private JTextField recipientField, messageField, searchField, deleteField;
    private JButton sendButton, searchButton, longestButton, deleteButton, reportButton;

    // Data Storage
    private String registeredUsername = "", registeredPassword = "", registeredPhone = "";
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> recipients = new ArrayList<>();
    private final ArrayList<String> messageIDs = new ArrayList<>();
    private final ArrayList<String> messageHashes = new ArrayList<>();
    private int messageCounter = 1;

    public QuickChatManagerApp() {
        setTitle("Quick Chat Manager");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel loginPanel = createLoginPanel();
        JPanel messagePanel = createMessageManagerPanel();

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(messagePanel, "Messages");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 30, 100, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 30, 180, 25);
        panel.add(usernameField);

        JLabel phoneLabel = new JLabel("Phone (+27...):");
        phoneLabel.setBounds(30, 70, 120, 25);
        panel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(150, 70, 180, 25);
        panel.add(phoneField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 110, 100, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 110, 180, 25);
        panel.add(passwordField);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(120, 160, 120, 35);
        panel.add(signUpButton);

        loginButton = new JButton("Login");
        loginButton.setBounds(120, 200, 120, 35);
        panel.add(loginButton);

        signUpButton.addActionListener(e -> handleSignUp());
        loginButton.addActionListener(e -> handleLogin());

        return panel;
    }

    private JPanel createMessageManagerPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        recipientField = new JTextField();
        messageField = new JTextField();
        inputPanel.add(new JLabel("Recipient:"));
        inputPanel.add(recipientField);
        inputPanel.add(new JLabel("Message:"));
        inputPanel.add(messageField);

        sendButton = new JButton("Send Message");
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(sendButton);

        panel.add(inputPanel, BorderLayout.NORTH);

        // Report Area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        // Controls
        JPanel controlPanel = new JPanel(new GridLayout(4, 2));
        searchField = new JTextField();
        deleteField = new JTextField();

        searchButton = new JButton("Search by Recipient/ID");
        searchButton.addActionListener(e -> searchMessages());

        longestButton = new JButton("Display Longest Message");
        longestButton.addActionListener(e -> displayLongestMessage());

        deleteButton = new JButton("Delete by Hash");
        deleteButton.addActionListener(e -> deleteByHash());

        reportButton = new JButton("Display Report");
        reportButton.addActionListener(e -> displayReport());

        controlPanel.add(new JLabel("Search (Recipient or ID):"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(longestButton);
        controlPanel.add(new JLabel("Delete (Message Hash):"));
        controlPanel.add(deleteField);
        controlPanel.add(deleteButton);
        controlPanel.add(reportButton);

        panel.add(controlPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void handleSignUp() {
        String username = usernameField.getText();
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());

        if (!isUsernameValid(username)) {
            JOptionPane.showMessageDialog(this, "Invalid username. Must contain '_' and be ≤ 5 characters.");
            return;
        }
        if (!isPasswordValid(password)) {
            JOptionPane.showMessageDialog(this, "Invalid password. Must be ≥ 8 chars, with uppercase, number, and symbol.");
            return;
        }
        if (!isPhoneValid(phone)) {
            JOptionPane.showMessageDialog(this, "Invalid phone. Must start with +27 and be 12 characters.");
            return;
        }

        registeredUsername = username;
        registeredPassword = password;
        registeredPhone = phone;
        JOptionPane.showMessageDialog(this, "Registration successful!");
    }

    private void handleLogin() {
        String username = JOptionPane.showInputDialog(this, "Enter your username:");
        String password = JOptionPane.showInputDialog(this, "Enter your password:");

        if (username.equals(registeredUsername) && password.equals(registeredPassword)) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome " + username + "!");
            cardLayout.show(mainPanel, "Messages");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect username or password.");
        }
    }

    private boolean isUsernameValid(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[^a-zA-Z0-9].*");
    }

    private boolean isPhoneValid(String phone) {
        return phone.matches("^\\+27\\d{9}$");
    }

    private void sendMessage() {
        String recipient = recipientField.getText().trim();
        String message = messageField.getText().trim();

        if (recipient.isEmpty() || message.isEmpty()) {
            showMessage("Recipient and message cannot be empty.");
            return;
        }

        String hash = Integer.toString(message.hashCode());
        String id = "MSG" + messageCounter++;

        sentMessages.add(message);
        messageHashes.add(hash);
        messageIDs.add(id);
        recipients.add(recipient);

        showMessage("Message sent!\nHash: " + hash + "\nID: " + id);
        recipientField.setText("");
        messageField.setText("");
    }

    private void searchMessages() {
        String searchKey = searchField.getText().trim();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).equalsIgnoreCase(searchKey) || messageIDs.get(i).equalsIgnoreCase(searchKey)) {
                result.append("To: ").append(recipients.get(i))
                        .append(" | ID: ").append(messageIDs.get(i))
                        .append(" | Message: ").append(sentMessages.get(i)).append("\n");
            }
        }

        showMessage(result.length() > 0 ? result.toString() : "No matching message found.");
    }

    private void displayLongestMessage() {
        String longest = "";
        for (String msg : sentMessages) {
            if (msg.length() > longest.length()) {
                longest = msg;
            }
        }
        showMessage(longest.isEmpty() ? "No messages to check." : "Longest Message:\n" + longest);
    }

    private void deleteByHash() {
        String hash = deleteField.getText().trim();
        int index = messageHashes.indexOf(hash);
        if (index >= 0) {
            String deletedMsg = sentMessages.get(index);

            sentMessages.remove(index);
            messageHashes.remove(index);
            messageIDs.remove(index);
            recipients.remove(index);

            showMessage("Deleted:\n" + deletedMsg);
        } else {
            showMessage("Hash not found.");
        }
    }

    private void displayReport() {
        if (sentMessages.isEmpty()) {
            showMessage("No messages to report.");
            return;
        }

        StringBuilder report = new StringBuilder();
        for (int i = 0; i < sentMessages.size(); i++) {
            report.append("Hash: ").append(messageHashes.get(i))
                    .append(" | ID: ").append(messageIDs.get(i))
                    .append(" | To: ").append(recipients.get(i))
                    .append(" | Message: ").append(sentMessages.get(i)).append("\n");
        }
        showMessage(report.toString());
    }

    private void showMessage(String msg) {
        reportArea.setText(msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuickChatManagerApp().setVisible(true));
    }
}

//References :
//OpenAI (2023) ChatGPT.[Medium]. Assistance with Java class structure and validation logic.
//Accessed via https://chatgpt on 16 June 2025.

