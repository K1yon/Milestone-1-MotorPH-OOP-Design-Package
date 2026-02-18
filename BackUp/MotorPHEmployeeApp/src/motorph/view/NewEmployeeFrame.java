package motorph.view;

import motorph.model.Employee;
import motorph.controller.EmployeeController;

import javax.swing.*;
import java.awt.*;

public class NewEmployeeFrame extends JFrame {
    private final EmployeeListFrame parentFrame;
    private final EmployeeController employeeController;
    private JTextField[] fields = new JTextField[19];

    public NewEmployeeFrame(EmployeeListFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.employeeController = parentFrame.employeeController;  // Use parent's controller
        
        setTitle("Add New Employee");
        setSize(500, 700);
        setLocationRelativeTo(parentFrame);
        setLayout(new GridLayout(20, 2, 5, 5));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] labels = {
            "Employee Number*", "Last Name*", "First Name*", "Birthday", "Address", "Phone Number", 
            "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number", "Status*", "Position*", 
            "Immediate Supervisor", "Basic Salary*", "Rice Subsidy", "Phone Allowance", 
            "Clothing Allowance", "Gross Semi-monthly Rate*", "Hourly Rate"
        };

        for (int i = 0; i < 19; i++) {
            JLabel label = new JLabel(labels[i] + ":");
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            add(label);
            fields[i] = new JTextField();
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            add(fields[i]);
        }

        add(new JLabel());
        JButton addButton = new JButton("Add Employee");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(addButton);

        addButton.addActionListener(e -> addEmployee());
    }

    private void addEmployee() {
        try {
            if (fields[0].getText().trim().isEmpty() || fields[1].getText().trim().isEmpty() ||
                fields[2].getText().trim().isEmpty() || fields[11].getText().trim().isEmpty() ||
                fields[13].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill required fields (*)");
                return;
            }

            Employee emp = new Employee(
                fields[0].getText().trim(), fields[1].getText().trim(), fields[2].getText().trim(),
                fields[3].getText().trim(), fields[4].getText().trim(), fields[5].getText().trim(),
                fields[6].getText().trim(), fields[7].getText().trim(), fields[8].getText().trim(),
                fields[9].getText().trim(), fields[10].getText().trim(), fields[11].getText().trim(),
                fields[12].getText().trim(), parseDoubleSafe(fields[13].getText()),
                parseDoubleSafe(fields[14].getText()), parseDoubleSafe(fields[15].getText()),
                parseDoubleSafe(fields[16].getText()), parseDoubleSafe(fields[17].getText()),
                parseDoubleSafe(fields[18].getText())
            );

            employeeController.createEmployee(emp);
            parentFrame.refreshEmployeeList();
            JOptionPane.showMessageDialog(this, "Employee added successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private double parseDoubleSafe(String value) {
        if (value == null) return 0.0;
        value = value.replace(",", "").replace("\"", "").trim();
        if (value.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}