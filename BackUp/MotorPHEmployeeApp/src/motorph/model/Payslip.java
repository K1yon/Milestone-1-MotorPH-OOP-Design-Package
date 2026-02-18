package motorph.model;
public class Payslip {

    private int payslipId;

    private String employeeNumber; 
  
    private double grossSalary;

    private double netSalary;
  
    private double govDeductions;

    public Payslip(int payslipId, String employeeNumber, double grossSalary, double govDeductions) {
        this.payslipId = payslipId;
        this.employeeNumber = employeeNumber;
        this.grossSalary = grossSalary;
        this.govDeductions = govDeductions;
        this.netSalary = calculateNetSalary();
    }

    public double calculateNetSalary() {
        return netSalary = grossSalary - govDeductions;
    }

    public void downloadCopy() {
        System.out.printf("Payslip #%d downloaded for Emp#%s (Net: PHP %.2f)%n", 
                         payslipId, employeeNumber, netSalary);
    }

    public int getPayslipId() { return payslipId; }
    public String getEmployeeNumber() { return employeeNumber; }
    public double getGrossSalary() { return grossSalary; }
    public double getNetSalary() { return netSalary; }
    public double getGovDeductions() { return govDeductions; }

    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
        calculateNetSalary();
    }
    
    public void setGovDeductions(double govDeductions) {
        this.govDeductions = govDeductions;
        calculateNetSalary();
    }

    @Override
    public String toString() {
        return String.format("Payslip #%d | Emp#%s | Gross: PHP %.2f | Net: PHP %.2f", 
                           payslipId, employeeNumber, grossSalary, netSalary);
    }
}