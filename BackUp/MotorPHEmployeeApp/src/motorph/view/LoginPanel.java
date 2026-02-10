package motorph.view;

import motorph.controller.LoginController;
import motorph.controller.UserSession; 

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField txtEmployeeNumber;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private LoginController loginController;
    private JFrame parentFrame;
    private UserSession userSession;  

    public LoginPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.loginController = new LoginController();
        this.userSession = new UserSession();

        setLayout(new GridBagLayout());
        initComponents();
        setupEventHandlers();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Employee Number:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEmployeeNumber = new JTextField(20);
        add(txtEmployeeNumber, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtPassword = new JPasswordField(20);
        add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        btnLogin = new JButton("Login");
        styleButton(btnLogin);
        add(btnLogin, gbc);
    }

    private void setupEventHandlers() {
        btnLogin.addActionListener(e -> performLogin());
        txtPassword.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String empNum = txtEmployeeNumber.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (empNum.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both fields.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (loginController.handleLogin(empNum, password)) {
            userSession.setCurrentUser(empNum);

            JOptionPane.showMessageDialog(this, "Login successful! Welcome " + empNum, "Success", JOptionPane.INFORMATION_MESSAGE);
            parentFrame.dispose();
            new EmployeeListFrame(loginController).setVisible(true);  // FIXED: Single constructor
            
            loginController.resetAttempts();
        } else {
            String message = String.format("Invalid credentials. Attempt %d/%d", 
                                         loginController.getAttemptCount(), 
                                         loginController.getMaxAttempts());
            
            JOptionPane.showMessageDialog(this, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            
            if (loginController.isLocked()) {
                JOptionPane.showMessageDialog(this, "Account locked. Contact administrator.", 
                                           "Access Denied", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(34, 81, 161));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 40));
    }
}