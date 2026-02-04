package motorph.model;

/**
 * Represents a payslip for an employee, including salary details and deductions.
 * <p>
 * This class encapsulates the payslip data and provides methods to calculate
 * net salary and simulate downloading a payslip copy.
 * </p>
 */
public class Payslip {

    /** Unique identifier for the payslip. */
    private int payslipId;

    /** Associated employee's ID. */
    private int employeeId;

    /** Total salary before deductions. */
    private double grossSalary;

    /** Salary after deductions. */
    private double netSalary;

    /** Government deductions (taxes, etc.). */
    private double govDeductions;

    /**
     * Constructs a Payslip with specified details.
     *
     * @param payslipId Unique payslip identifier.
     * @param employeeId Employee's ID.
     * @param grossSalary Gross salary amount.
     * @param govDeductions Government deductions amount.
     */
    public Payslip(int payslipId, int employeeId, double grossSalary, double govDeductions) {
        this.payslipId = payslipId;
        this.employeeId = employeeId;
        this.grossSalary = grossSalary;
        this.govDeductions = govDeductions;
        this.netSalary = calculateNetSalary();
    }

    /**
     * Calculates the net salary after deducting government deductions.
     *
     * @return The net salary amount.
     */
    public double calculateNetSalary() {
        netSalary = grossSalary - govDeductions;
        return netSalary;
    }

    /**
     * Simulates downloading a copy of the payslip.
     * In a real application, this might generate a PDF or send an email.
     */
    public void downloadCopy() {
        // Placeholder for download logic
        System.out.println("Payslip copy downloaded for employee ID: " + employeeId);
    }

    // Getters and setters

    public int getPayslipId() {
        return payslipId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public double getGovDeductions() {
        return govDeductions;
    }

    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
        calculateNetSalary(); // Recalculate net salary when gross changes
    }

    public void setGovDeductions(double govDeductions) {
        this.govDeductions = govDeductions;
        calculateNetSalary(); // Recalculate net salary when deductions change
    }
}