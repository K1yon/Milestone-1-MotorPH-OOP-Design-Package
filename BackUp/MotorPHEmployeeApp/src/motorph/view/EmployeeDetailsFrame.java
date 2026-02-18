package motorph.view;

import motorph.model.Employee;
import motorph.model.LeaveRequest;
import motorph.model.WorkHours;
import motorph.util.AttendanceReader;
import motorph.util.PayrollCalculator;
import motorph.service.LeaveService;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EmployeeDetailsFrame extends JFrame {

    private final Employee employee;
    private JComboBox<String> monthCombo;
    private JButton computeButton, closeButton;
    private JTextArea salaryArea;
    private AttendanceReader attendanceReader;
    private JButton requestLeaveButton;
    private JButton viewLeaveStatusButton;

    public EmployeeDetailsFrame(JFrame parent, Employee emp) {
        this.employee = emp;
        this.attendanceReader = new AttendanceReader();

        setTitle("Employee Details: " + emp.getFirstName() + " " + emp.getLastName());
        setSize(650, 800);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(242, 247, 255));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);

        JLabel monthLabel = new JLabel("Select Month:");
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        monthCombo = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        computeButton = new JButton("Compute Salary");
        styleAccentButton(computeButton);

        topPanel.add(monthLabel);
        topPanel.add(monthCombo);
        topPanel.add(computeButton);

        requestLeaveButton = new JButton("Request Leave");
        styleAccentButton(requestLeaveButton);
        topPanel.add(requestLeaveButton);

        viewLeaveStatusButton = new JButton("My Leave Status");
        styleAccentButton(viewLeaveStatusButton);
        topPanel.add(viewLeaveStatusButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel personalPanel = createCardPanel("Personal Information");
        addLabelAndField(personalPanel, "Employee Number:", emp.getEmployeeNumber());
        addLabelAndField(personalPanel, "Last Name:", emp.getLastName());
        addLabelAndField(personalPanel, "First Name:", emp.getFirstName());
        addLabelAndField(personalPanel, "Birthday:", emp.getBirthday());
        addLabelAndField(personalPanel, "Address:", emp.getAddress());
        addLabelAndField(personalPanel, "Phone Number:", emp.getPhoneNumber());

        JPanel govPanel = createCardPanel("Government IDs");
        addLabelAndField(govPanel, "SSS Number:", emp.getSssNumber());
        addLabelAndField(govPanel, "PhilHealth Number:", emp.getPhilhealthNumber());
        addLabelAndField(govPanel, "TIN Number:", emp.getTinNumber());
        addLabelAndField(govPanel, "Pag-IBIG Number:", emp.getPagibigNumber());

        JPanel employmentPanel = createCardPanel("Employment Information");
        addLabelAndField(employmentPanel, "Status:", emp.getStatus());
        addLabelAndField(employmentPanel, "Position:", emp.getPosition());
        addLabelAndField(employmentPanel, "Immediate Supervisor:", emp.getImmediateSupervisor());

        JPanel salaryPanel = createCardPanel("Salary Details");
        addLabelAndField(salaryPanel, "Basic Salary:", String.format("%,.2f", emp.getBasicSalary()));
        addLabelAndField(salaryPanel, "Rice Subsidy:", String.format("%,.2f", emp.getRiceSubsidy()));
        addLabelAndField(salaryPanel, "Phone Allowance:", String.format("%,.2f", emp.getPhoneAllowance()));
        addLabelAndField(salaryPanel, "Clothing Allowance:", String.format("%,.2f", emp.getClothingAllowance()));
        addLabelAndField(salaryPanel, "Gross Semi-monthly Rate:", String.format("%,.2f", emp.getGrossSemiMonthlyRate()));
        addLabelAndField(salaryPanel, "Hourly Rate:", String.format("%,.2f", emp.getHourlyRate()));

        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setOpaque(false);
        cardsContainer.add(personalPanel);
        cardsContainer.add(Box.createVerticalStrut(15));
        cardsContainer.add(govPanel);
        cardsContainer.add(Box.createVerticalStrut(15));
        cardsContainer.add(employmentPanel);
        cardsContainer.add(Box.createVerticalStrut(15));
        cardsContainer.add(salaryPanel);

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);

        salaryArea = new JTextArea(18, 40);
        salaryArea.setEditable(false);
        salaryArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        salaryArea.setBackground(Color.WHITE);
        salaryArea.setBorder(BorderFactory.createTitledBorder("Payroll Computation"));
        bottomPanel.add(new JScrollPane(salaryArea), BorderLayout.CENTER);

        closeButton = new JButton("Close");
        styleAccentButton(closeButton);

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closePanel.setOpaque(false);
        closePanel.add(closeButton);
        bottomPanel.add(closePanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Auto-show notification on open if any approved/denied requests
        showLeaveNotifications();

        computeButton.addActionListener(e -> {
            String month = (String) monthCombo.getSelectedItem();
            int monthNumber = monthCombo.getSelectedIndex() + 1;
            salaryArea.setText(computeFullPayroll(employee, month, monthNumber));
        });

        requestLeaveButton.addActionListener(e -> {
            JTextField fromDate = new JTextField();
            JTextField toDate = new JTextField();
            JComboBox<String> leaveTypeCombo = new JComboBox<>(new String[]{
                "Sick Leave",
                "Vacation Leave",
                "Emergency Leave",
                "Maternity Leave",
                "Paternity Leave"
            });

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Leave Type:"));
            panel.add(leaveTypeCombo);
            panel.add(new JLabel("From (YYYY-MM-DD):"));
            panel.add(fromDate);
            panel.add(new JLabel("To (YYYY-MM-DD):"));
            panel.add(toDate);

            int result = JOptionPane.showConfirmDialog(
                this, panel, "Leave Request", JOptionPane.OK_CANCEL_OPTION
            );

            if (result == JOptionPane.OK_OPTION) {
                try {
                    new LeaveService().submitRequest(
                        employee.getEmployeeNumber(),
                        (String) leaveTypeCombo.getSelectedItem(),
                        fromDate.getText().trim(),
                        toDate.getText().trim()
                    );
                    JOptionPane.showMessageDialog(this, "Leave request submitted.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error submitting leave request: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        viewLeaveStatusButton.addActionListener(e -> showLeaveStatusDialog());

        closeButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void showLeaveNotifications() {
        try {
            List<LeaveRequest> requests = new LeaveService()
                .getRequestsByEmployee(employee.getEmployeeNumber());

            StringBuilder approved = new StringBuilder();
            StringBuilder denied = new StringBuilder();

            for (LeaveRequest r : requests) {
                if (r.getStatus().equalsIgnoreCase("APPROVED")) {
                    approved.append("  - ").append(r.getLeaveType()).append("\n");
                } else if (r.getStatus().equalsIgnoreCase("DENIED")) {
                    denied.append("  - ").append(r.getLeaveType()).append("\n");
                }
            }

            if (approved.length() > 0 || denied.length() > 0) {
                StringBuilder message = new StringBuilder("Leave Request Update:\n\n");
                if (approved.length() > 0) {
                    message.append("APPROVED:\n").append(approved).append("\n");
                }
                if (denied.length() > 0) {
                    message.append("DENIED:\n").append(denied);
                }

                JOptionPane.showMessageDialog(this,
                    message.toString(),
                    "Leave Notification",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            System.err.println("Could not load leave notifications: " + e.getMessage());
        }
    }

    // Full status dialog showing all requests
    private void showLeaveStatusDialog() {
        try {
            List<LeaveRequest> requests = new LeaveService()
                .getRequestsByEmployee(employee.getEmployeeNumber());

            if (requests.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "You have no leave requests on record.",
                    "My Leave Status",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] columns = {"Leave Type", "Status"};
            Object[][] data = new Object[requests.size()][2];
            for (int i = 0; i < requests.size(); i++) {
                LeaveRequest r = requests.get(i);
                data[i][0] = r.getLeaveType();
                data[i][1] = r.getStatus();
            }

            JTable table = new JTable(data, columns) {
                @Override
                public boolean isCellEditable(int row, int col) { return false; }

                @Override
                public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int col) {
                    Component c = super.prepareRenderer(renderer, row, col);
                    String status = (String) getValueAt(row, 1);
                    if ("APPROVED".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(198, 239, 206));
                        c.setForeground(new Color(0, 97, 0));
                    } else if ("DENIED".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(255, 199, 206));
                        c.setForeground(new Color(156, 0, 6));
                    } else {
                        c.setBackground(new Color(255, 235, 156));
                        c.setForeground(new Color(156, 101, 0));
                    }
                    return c;
                }
            };

            table.setRowHeight(28);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

            JScrollPane sp = new JScrollPane(table);
            sp.setPreferredSize(new Dimension(450, 200));

            JOptionPane.showMessageDialog(this, sp,
            "My Leave Status - " + employee.getFirstName() + " " + employee.getLastName(),
                JOptionPane.PLAIN_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading leave status: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    private void addLabelAndField(JPanel panel, String labelText, String value) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        gbc.gridx = 0;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField field = new JTextField(value);
        field.setEditable(false);
        field.setBackground(new Color(247, 250, 255));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        panel.add(field, gbc);
    }

    private void styleAccentButton(JButton btn) {
        btn.setBackground(new Color(34, 81, 161));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(24, 61, 121));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(34, 81, 161));
            }
        });
    }

    private String computeFullPayroll(Employee emp, String monthName, int monthNumber) {
        List<WorkHours> monthlyAttendance = attendanceReader.getAttendanceForMonth(
            emp.getEmployeeNumber(), monthNumber);

        int totalRegularHours = 0;
        int totalOvertimeHours = 0;
        for (WorkHours wh : monthlyAttendance) {
            totalRegularHours += wh.getRegularHours();
            totalOvertimeHours += wh.getOvertimeHours();
        }

        double regularPay = totalRegularHours * emp.getHourlyRate();
        double overtimePay = totalOvertimeHours * emp.getHourlyRate() * 1.25;
        double totalAllowances = emp.getRiceSubsidy() + emp.getPhoneAllowance() + emp.getClothingAllowance();
        double grossPay = regularPay + overtimePay + totalAllowances;

        PayrollCalculator calculator = new PayrollCalculator();
        double sssDeduction = calculator.calculateSSS(emp.getBasicSalary());
        double philhealthDeduction = calculator.calculatePhilHealth(emp.getBasicSalary());
        double pagibigDeduction = calculator.calculatePagIbig(emp.getBasicSalary());
        double withholdingTax = calculator.calculateWithholdingTax(grossPay);

        double totalDeductions = sssDeduction + philhealthDeduction + pagibigDeduction + withholdingTax;
        double netPay = grossPay - totalDeductions;

        return String.format(
            "╔═══════════════════════════════════════════════════════╗%n" +
            "║ PAYROLL COMPUTATION - %s ║%n" +
            "╠═══════════════════════════════════════════════════════╣%n" +
            "║ ATTENDANCE SUMMARY ║%n" +
            "╠═══════════════════════════════════════════════════════╣%n" +
            " Regular Hours: %8d hrs%n" +
            " Overtime Hours: %8d hrs%n" +
            " Total Hours Worked: %8d hrs%n" +
            "%n" +
            "╔═══════════════════════════════════════════════════════╗%n" +
            "║ EARNINGS ║%n" +
            "╠═══════════════════════════════════════════════════════╣%n" +
            " Regular Pay: %,15.2f%n" +
            " Overtime Pay: %,15.2f%n" +
            " ──────────────────────────────────────────────────────%n" +
            " Subtotal: %,15.2f%n" +
            "%n" +
            " ALLOWANCES:%n" +
            " Rice Subsidy: %,15.2f%n" +
            " Phone Allowance: %,15.2f%n" +
            " Clothing Allowance: %,15.2f%n" +
            " ──────────────────────────────────────────────────────%n" +
            " Total Allowances: %,15.2f%n" +
            " ══════════════════════════════════════════════════════%n" +
            " GROSS PAY: %,15.2f%n" +
            "%n" +
            "╔═══════════════════════════════════════════════════════╗%n" +
            "║ DEDUCTIONS ║%n" +
            "╠═══════════════════════════════════════════════════════╣%n" +
            " SSS Contribution: %,15.2f%n" +
            " PhilHealth Contribution: %,15.2f%n" +
            " Pag-IBIG Contribution: %,15.2f%n" +
            " Withholding Tax: %,15.2f%n" +
            " ──────────────────────────────────────────────────────%n" +
            " Total Deductions: %,15.2f%n" +
            "%n" +
            "╔═══════════════════════════════════════════════════════╗%n" +
            "║ NET PAY ║%n" +
            "╠═══════════════════════════════════════════════════════╣%n" +
            " NET SALARY: %,15.2f%n" +
            "╚═══════════════════════════════════════════════════════╝%n",
            monthName,
            totalRegularHours,
            totalOvertimeHours,
            totalRegularHours + totalOvertimeHours,
            regularPay,
            overtimePay,
            regularPay + overtimePay,
            emp.getRiceSubsidy(),
            emp.getPhoneAllowance(),
            emp.getClothingAllowance(),
            totalAllowances,
            grossPay,
            sssDeduction,
            philhealthDeduction,
            pagibigDeduction,
            withholdingTax,
            totalDeductions,
            netPay
        );
    }
}