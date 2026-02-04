package motorph.view;

import motorph.model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

public class EmployeeListFrame extends JPanel {

    private DefaultTableModel model;

    public EmployeeListFrame() {
        setLayout(new java.awt.BorderLayout());

        model = new DefaultTableModel(new String[]{
                "Employee ID", "Name", "Position",
                "Gross Pay", "Deductions", "Net Pay"
        }, 0);

        JTable table = new JTable(model);
        add(new JScrollPane(table), java.awt.BorderLayout.CENTER);

        loadEmployees();
    }

    private void loadEmployees() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getClass().getClassLoader()
                                .getResourceAsStream("employee_details.csv")))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");

                Employee emp = new Employee(
                        d[0], d[2], d[1], d[11],
                        parseMoney(d[13]),
                        parseMoney(d[14]),
                        parseMoney(d[15]),
                        parseMoney(d[16])
                );

                model.addRow(new Object[]{
                        emp.getEmployeeNumber(),
                        emp.getFullName(),
                        emp.getPosition(),
                        emp.calculateGrossPay(),
                        emp.calculateDeductions(),
                        emp.calculateNetPay()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading CSV: " + e.getMessage());
        }
    }

    private double parseMoney(String val) {
        return Double.parseDouble(val.replace(",", ""));
    }
}
