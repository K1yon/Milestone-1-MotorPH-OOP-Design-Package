package motorph.model;

public class Employee {

    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String position;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;

    public Employee(String employeeNumber, String firstName, String lastName,
                    String position, double basicSalary,
                    double riceSubsidy, double phoneAllowance,
                    double clothingAllowance) {

        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
    }

    // === Encapsulation ===
    public String getEmployeeNumber() { return employeeNumber; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getPosition() { return position; }

    // === Polymorphic payroll method (MS1) ===
    public double calculateGrossPay() {
        return basicSalary + riceSubsidy + phoneAllowance + clothingAllowance;
    }

    // === Shared helper (MS1) ===
    public double calculateDeductions() {
        return basicSalary * 0.12; // simplified statutory deduction
    }

    public double calculateNetPay() {
        return calculateGrossPay() - calculateDeductions();
    }
}
