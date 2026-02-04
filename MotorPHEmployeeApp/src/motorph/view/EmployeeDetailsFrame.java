package motorph.view;

import motorph.model.Employee;
import javax.swing.*;

public class EmployeeDetailsFrame extends JFrame {

    public EmployeeDetailsFrame(Employee emp) {
        setTitle("Employee Payroll Details");
        setSize(400, 300);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        area.setText(
                "Employee: " + emp.getFullName() + "\n" +
                "Position: " + emp.getPosition() + "\n\n" +
                "Gross Pay: " + emp.calculateGrossPay() + "\n" +
                "Deductions: " + emp.calculateDeductions() + "\n" +
                "Net Pay: " + emp.calculateNetPay()
        );

        add(new JScrollPane(area));
    }
}
