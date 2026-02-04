package motorph.view;

import motorph.controller.LoginController;
import javax.swing.*;

public class LoginPanel extends JFrame {

    public LoginPanel() {
        setTitle("MotorPH Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextField empField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        setLayout(new java.awt.GridLayout(3, 2));
        add(new JLabel("Employee ID"));
        add(empField);
        add(new JLabel("Password"));
        add(passField);
        add(new JLabel());
        add(loginBtn);

        loginBtn.addActionListener(e -> {
            if (LoginController.authenticate(
                    empField.getText(),
                    new String(passField.getPassword()))) {

                dispose();
                new MainFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login");
            }
        });
    }
}
