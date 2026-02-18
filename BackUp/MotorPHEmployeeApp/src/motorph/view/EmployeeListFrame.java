package motorph.view;

import motorph.controller.LoginController;
import motorph.controller.EmployeeController;
import motorph.model.Employee;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.regex.Pattern;

public class EmployeeListFrame extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private java.util.List<Employee> employeeList = new ArrayList<>();
    private final LoginController loginController;
    public final EmployeeController employeeController;
    private final String csvFilePath = "D:\\Code Projects\\BackUp\\MotorPHEmployeeApp\\employee_data_full.csv";

    private JTextField searchField;
    private JComboBox<String> positionFilterCombo;
    private JComboBox<String> statusFilterCombo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton viewButton, newButton, deleteButton, updateButton;
    private JButton leaveRequestsButton;

    public EmployeeListFrame(LoginController loginController) {
        this.loginController = loginController;
        this.employeeController = new EmployeeController(csvFilePath);

        setTitle("Employee List");
        setSize(1400, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        loadEmployees();
        setupEventHandlers();
    }

    private void initComponents() {
        String[] columns = {
            "Employee Number", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS Number",
            "PhilHealth Number", "TIN", "Pag-IBIG Number", "Status", "Position", "Immediate Supervisor",
            "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance",
            "Gross Semi-monthly Rate", "Hourly Rate"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(employeeTable);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(30);
        searchPanel.add(new JLabel("Search (ID, Last, First): "));
        searchPanel.add(searchField);

        positionFilterCombo = new JComboBox<>();
        positionFilterCombo.addItem("All Positions");
        statusFilterCombo = new JComboBox<>();
        statusFilterCombo.addItem("All Statuses");

        searchPanel.add(new JLabel("Position:"));
        searchPanel.add(positionFilterCombo);
        searchPanel.add(new JLabel("Status:"));
        searchPanel.add(statusFilterCombo);

        JPanel buttonPanel = new JPanel();
        viewButton = new JButton("View Employee");
        newButton = new JButton("New Employee");
        deleteButton = new JButton("Delete Employee");
        updateButton = new JButton("Update Employee");

        boolean isHR = loginController.isHRAdmin();
        newButton.setEnabled(isHR);
        deleteButton.setEnabled(false);
        updateButton.setEnabled(isHR);
        viewButton.setEnabled(false);

        buttonPanel.add(viewButton);
        buttonPanel.add(newButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        leaveRequestsButton = new JButton("Leave Requests");
        leaveRequestsButton.setEnabled(loginController.isHRAdmin());
        buttonPanel.add(leaveRequestsButton);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        populateFilterCombos();
    }

    private void loadEmployees() {
        employeeList.clear();
        tableModel.setRowCount(0);
        try {
            employeeList.addAll(employeeController.loadAllEmployees());
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Employee emp : employeeList) {
            tableModel.addRow(new Object[]{
                emp.getEmployeeNumber(), emp.getLastName(), emp.getFirstName(), emp.getBirthday(),
                emp.getAddress(), emp.getPhoneNumber(), emp.getSssNumber(), emp.getPhilhealthNumber(),
                emp.getTinNumber(), emp.getPagibigNumber(), emp.getStatus(), emp.getPosition(),
                emp.getImmediateSupervisor(), emp.getBasicSalary(), emp.getRiceSubsidy(),
                emp.getPhoneAllowance(), emp.getClothingAllowance(), emp.getGrossSemiMonthlyRate(),
                emp.getHourlyRate()
            });
        }
        populateFilterCombos();
    }

    private void populateFilterCombos() {
        positionFilterCombo.removeAllItems();
        statusFilterCombo.removeAllItems();
        positionFilterCombo.addItem("All Positions");
        statusFilterCombo.addItem("All Statuses");

        Set<String> positions = new TreeSet<>();
        Set<String> statuses = new TreeSet<>();
        for (Employee emp : employeeList) {
            positions.add(emp.getPosition());
            statuses.add(emp.getStatus());
        }
        for (String pos : positions) positionFilterCombo.addItem(pos);
        for (String stat : statuses) statusFilterCombo.addItem(stat);
    }

    private void applyFilters() {
        String text = searchField.getText().trim();
        String selectedPosition = (String) positionFilterCombo.getSelectedItem();
        String selectedStatus = (String) statusFilterCombo.getSelectedItem();

        java.util.List<RowFilter<Object, Object>> filters = new ArrayList<>();
        if (!text.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(text), 0, 1, 2));
        }
        if (selectedPosition != null && !selectedPosition.equals("All Positions")) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(selectedPosition) + "$", 11));
        }
        if (selectedStatus != null && !selectedStatus.equals("All Statuses")) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(selectedStatus) + "$", 10));
        }
        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void setupEventHandlers() {
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = employeeTable.getSelectedRow() >= 0;
            viewButton.setEnabled(selected);
            if (loginController.isHRAdmin()) {
                deleteButton.setEnabled(selected);
                updateButton.setEnabled(selected);
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });

        positionFilterCombo.addActionListener(e -> applyFilters());
        statusFilterCombo.addActionListener(e -> applyFilters());

        viewButton.addActionListener(e -> openDetails());
        newButton.addActionListener(e -> openNewEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        updateButton.addActionListener(e -> openUpdateDialog());

        leaveRequestsButton.addActionListener(e -> new HRLeaveFrame(this));
    }

    private Employee getSelectedEmployee() {
        int viewRow = employeeTable.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = employeeTable.convertRowIndexToModel(viewRow);
            return employeeList.get(modelRow);
        }
        return null;
    }

    private void openDetails() {
        Employee emp = getSelectedEmployee();
        if (emp != null) {
            new EmployeeDetailsFrame(this, emp);
        }
    }

    private void openNewEmployee() {
        new NewEmployeeFrame(this);
    }

    private void deleteEmployee() {
        Employee emp = getSelectedEmployee();
        if (emp != null && JOptionPane.showConfirmDialog(this,
            "Delete " + emp.getLastName() + ", " + emp.getFirstName() + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            employeeController.deleteEmployee(emp.getEmployeeNumber());
            loadEmployees();
            JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
        }
    }

    private void openUpdateDialog() {
        Employee emp = getSelectedEmployee();
        if (emp == null) return;

        JTextField txtFirstName = new JTextField(emp.getFirstName());
        JTextField txtLastName = new JTextField(emp.getLastName());
        JTextField txtPosition = new JTextField(emp.getPosition());
        JTextField txtStatus = new JTextField(emp.getStatus());
        JTextField txtBasicSalary = new JTextField(String.valueOf(emp.getBasicSalary()));

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("First Name:")); panel.add(txtFirstName);
        panel.add(new JLabel("Last Name:")); panel.add(txtLastName);
        panel.add(new JLabel("Position:")); panel.add(txtPosition);
        panel.add(new JLabel("Status:")); panel.add(txtStatus);
        panel.add(new JLabel("Basic Salary:")); panel.add(txtBasicSalary);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                emp.setFirstName(txtFirstName.getText().trim());
                emp.setLastName(txtLastName.getText().trim());
                emp.setPosition(txtPosition.getText().trim());
                emp.setStatus(txtStatus.getText().trim());
                emp.setBasicSalary(Double.parseDouble(txtBasicSalary.getText().trim()));
                employeeController.updateEmployee(emp);
                loadEmployees();
                JOptionPane.showMessageDialog(this, "Employee updated successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary input.");
            }
        }
    }

    private double parseDoubleSafe(String val) {
        try {
            return Double.parseDouble(val.replace(",", "").trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public void refreshEmployeeList() {
        loadEmployees();
        applyFilters();
    }
}