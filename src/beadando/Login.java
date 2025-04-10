package beadando;


import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JDialog {
    private boolean successfulLogin = false;

    public boolean isSuccessfulLogin() {
        return successfulLogin;
    }

    public Login(Frame parent) {
        super(parent, "Bejelentkezés", true);
        setLayout(new GridLayout(3, 2));

        JLabel messageLabel = new JLabel();
        JLabel userLabel = new JLabel("Felhasználónév:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Jelszó:");
        JPasswordField passField = new JPasswordField();
        passField.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("admin")) {
                successfulLogin = true;
                dispose(); 
            } else {
                messageLabel.setText("Hibás adatok");
                messageLabel.setForeground(Color.RED);
            }
        });
        JButton loginButton = new JButton("Bejelentkezés");

        add(userLabel); add(userField);
        add(passLabel); add(passField);
        add(loginButton); add(messageLabel);

        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("admin")) {
                successfulLogin = true;
                dispose(); 
            } else {
                messageLabel.setText("Hibás adatok");
                messageLabel.setForeground(Color.RED);
            }
        });

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}

