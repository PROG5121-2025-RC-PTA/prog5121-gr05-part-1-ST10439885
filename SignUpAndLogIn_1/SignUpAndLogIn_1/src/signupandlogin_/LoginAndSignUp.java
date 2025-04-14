package signupandlogin_;

import javax.swing.*;
import java.awt.event.*;
import java.util.regex.*;

public class LoginAndSignUp extends JFrame {
    private JTextField usernameField, phoneField;
    private JPasswordField passwordField;
    private JButton signUpButton, loginButton;
    
    private String registeredUsername = "";
    private String registeredPassword = "";
    private String registeredPhone = "";

    public LoginAndSignUp() {
        setTitle("User Registration and Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Registration UI components
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 30, 100, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 30, 180, 25);
        panel.add(usernameField);

        JLabel phoneLabel = new JLabel("Phone (+27...):");
        phoneLabel.setBounds(30, 70, 100, 25);
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

        // Sign Up button action listener
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });

        // Login button action listener
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void handleSignUp() {
        String username = usernameField.getText();
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());

        StringBuilder message = new StringBuilder();

        if (isUsernameValid(username)) {
            registeredUsername = username;
            System.out.println("Username successfully captured.\n");
        } else {
            System.out.println("Username is not correctly formatted. It must contain an underscore and be no more than 5 characters.\n");
        }

        if (isPasswordValid(password)) {
            registeredPassword = password;
            System.out.println("Password successfully captured.\n");
        } else {
            System.out.println("Password must have at least 8 characters, 1 uppercase letter, 1 number, and 1 special character.\n");
        }

        if (isPhoneValid(phone)) {
            registeredPhone = phone;
            System.out.println("Cell phone number successfully captured.");
        } else {
            System.out.println("Cell number must start with +27 and be 12 digits long.");
        }

        JOptionPane.showMessageDialog(this, message.toString());
    }

    private void handleLogin() {
        String username = JOptionPane.showInputDialog(this, "Enter your username:");
        String password = JOptionPane.showInputDialog(this, "Enter your password:");

        if (username.equals(registeredUsername) && password.equals(registeredPassword)) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome Back, " + username + "!");
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect username or password.");
        }
    }

    // === Validation Methods ===
    private boolean isUsernameValid(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 &&
               Pattern.compile("[A-Z]").matcher(password).find() &&
               Pattern.compile("[0-9]").matcher(password).find() &&
               Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
    }

    private boolean isPhoneValid(String phone) {
        return phone.matches("^\\+27\\d{9}$");
    }

    // === Main Method ===
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginAndSignUp());
    }
}