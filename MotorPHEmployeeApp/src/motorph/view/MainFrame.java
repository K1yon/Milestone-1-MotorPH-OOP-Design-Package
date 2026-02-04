package motorph.view;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("MotorPH Payroll System");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new EmployeeListFrame());
    }
}
