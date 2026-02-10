package motorph.controller;

import motorph.model.Employee;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class EmployeeController {

    private final String csvFilePath;
    private final List<Employee> employeeList = new ArrayList<>();

    public EmployeeController(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        loadAllEmployees();
    }

    public List<Employee> loadAllEmployees() {
        employeeList.clear();

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(new FileInputStream(csvFilePath), StandardCharsets.UTF_8))) {

            String[] line;
            boolean firstLine = true;

            while ((line = reader.readNext()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.length < 19) {
                    continue;
                }

                Employee emp = new Employee(
                        line[0],  // Employee Number
                        line[1],  // Last Name
                        line[2],  // First Name
                        line[3],  // Birthday
                        line[4],  // Address
                        line[5],  // Phone Number
                        line[6],  // SSS
                        line[7],  // PhilHealth
                        line[8],  // TIN
                        line[9],  // Pag-IBIG
                        line[10], // Status
                        line[11], // Position
                        line[12], // Immediate Supervisor
                        parseDoubleSafe(line[13]), // Basic Salary
                        parseDoubleSafe(line[14]), // Rice Subsidy
                        parseDoubleSafe(line[15]), // Phone Allowance
                        parseDoubleSafe(line[16]), // Clothing Allowance
                        parseDoubleSafe(line[17]), // Gross Semi-monthly Rate
                        parseDoubleSafe(line[18])  // Hourly Rate
                );

                employeeList.add(emp);
            }

        } catch (Exception ex) {
            System.err.println("Error loading employees CSV: " + ex.getMessage());
        }

        return new ArrayList<>(employeeList);
    }

    public void createEmployee(Employee emp) {
        employeeList.add(emp);
        saveAllEmployees();
    }

    public void deleteEmployee(String employeeNumber) {
        employeeList.removeIf(emp ->
                emp.getEmployeeNumber().equals(employeeNumber));
        saveAllEmployees();
    }

    public void updateEmployee(Employee updatedEmp) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getEmployeeNumber()
                    .equals(updatedEmp.getEmployeeNumber())) {

                employeeList.set(i, updatedEmp);
                saveAllEmployees();
                return;
            }
        }
    }

    private void saveAllEmployees() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {

            writer.writeNext(new String[]{
                    "Employee Number", "Last Name", "First Name", "Birthday", "Address",
                    "Phone Number", "SSS Number", "PhilHealth Number", "TIN",
                    "Pag-IBIG Number", "Status", "Position", "Immediate Supervisor",
                    "Basic Salary", "Rice Subsidy", "Phone Allowance",
                    "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"
            });

            for (Employee emp : employeeList) {
                writer.writeNext(new String[]{
                        emp.getEmployeeNumber(),
                        emp.getLastName(),
                        emp.getFirstName(),
                        emp.getBirthday(),
                        emp.getAddress(),
                        emp.getPhoneNumber(),
                        emp.getSssNumber(),
                        emp.getPhilhealthNumber(),
                        emp.getTinNumber(),
                        emp.getPagibigNumber(),
                        emp.getStatus(),
                        emp.getPosition(),
                        emp.getImmediateSupervisor(),
                        String.valueOf(emp.getBasicSalary()),
                        String.valueOf(emp.getRiceSubsidy()),
                        String.valueOf(emp.getPhoneAllowance()),
                        String.valueOf(emp.getClothingAllowance()),
                        String.valueOf(emp.getGrossSemiMonthlyRate()),
                        String.valueOf(emp.getHourlyRate())
                });
            }

        } catch (IOException e) {
            System.err.println("Error saving employees CSV: " + e.getMessage());
        }
    }

    private static double parseDoubleSafe(String val) {
        if (val == null) {
            return 0.0;
        }

        try {
            String cleaned = val
                    .replace("\"", "")
                    .replace(",", "")
                    .trim();

            if (cleaned.isEmpty()) {
                return 0.0;
            }

            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}