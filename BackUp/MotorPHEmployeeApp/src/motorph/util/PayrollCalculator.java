package motorph.util;

public class PayrollCalculator {

    public double calculateSSS(double monthlySalary) {
        if (monthlySalary < 4250) return 180.00;
        if (monthlySalary < 4750) return 202.50;
        if (monthlySalary < 5250) return 225.00;
        if (monthlySalary < 5750) return 247.50;
        if (monthlySalary < 6250) return 270.00;
        if (monthlySalary < 6750) return 292.50;
        if (monthlySalary < 7250) return 315.00;
        if (monthlySalary < 7750) return 337.50;
        if (monthlySalary < 8250) return 360.00;
        if (monthlySalary < 8750) return 382.50;
        if (monthlySalary < 9250) return 405.00;
        if (monthlySalary < 9750) return 427.50;
        if (monthlySalary < 10250) return 450.00;
        if (monthlySalary < 10750) return 472.50;
        if (monthlySalary < 11250) return 495.00;
        if (monthlySalary < 11750) return 517.50;
        if (monthlySalary < 12250) return 540.00;
        if (monthlySalary < 12750) return 562.50;
        if (monthlySalary < 13250) return 585.00;
        if (monthlySalary < 13750) return 607.50;
        if (monthlySalary < 14250) return 630.00;
        if (monthlySalary < 14750) return 652.50;
        if (monthlySalary < 15250) return 675.00;
        if (monthlySalary < 15750) return 697.50;
        if (monthlySalary < 16250) return 720.00;
        if (monthlySalary < 16750) return 742.50;
        if (monthlySalary < 17250) return 765.00;
        if (monthlySalary < 17750) return 787.50;
        if (monthlySalary < 18250) return 810.00;
        if (monthlySalary < 18750) return 832.50;
        if (monthlySalary < 19250) return 855.00;
        if (monthlySalary < 19750) return 877.50;
        if (monthlySalary >= 19750) return 900.00;
        
        return 0;
    }

    public double calculatePhilHealth(double monthlySalary) {
        double premiumRate = 0.05;
        double employeeShare = 0.50; 
        
        double minSalary = 10000;
        double maxSalary = 100000;
        
        double baseSalary = monthlySalary;
        if (baseSalary < minSalary) baseSalary = minSalary;
        if (baseSalary > maxSalary) baseSalary = maxSalary;
        
        return (baseSalary * premiumRate * employeeShare);
    }
  
    public double calculatePagIbig(double monthlySalary) {
        if (monthlySalary <= 1500) {
            return monthlySalary * 0.01; // 1%
        } else {
            return monthlySalary * 0.02; // 2%
        }
    }
  
    public double calculateWithholdingTax(double monthlyTaxableIncome) {
        double annualIncome = monthlyTaxableIncome * 12;
        double annualTax = 0;
        
        if (annualIncome <= 250000) {
            annualTax = 0;
        } else if (annualIncome <= 400000) {
            annualTax = (annualIncome - 250000) * 0.15;
        } else if (annualIncome <= 800000) {
            annualTax = 22500 + (annualIncome - 400000) * 0.20;
        } else if (annualIncome <= 2000000) {
            annualTax = 102500 + (annualIncome - 800000) * 0.25;
        } else if (annualIncome <= 8000000) {
            annualTax = 402500 + (annualIncome - 2000000) * 0.30;
        } else {
            annualTax = 2202500 + (annualIncome - 8000000) * 0.35;
        }
        
        return annualTax / 12; // Monthly withholding tax
    }
    
    public double calculateTotalDeductions(double monthlySalary, double grossPay) {
        return calculateSSS(monthlySalary) + 
               calculatePhilHealth(monthlySalary) + 
               calculatePagIbig(monthlySalary) +
               calculateWithholdingTax(grossPay);
    }
}