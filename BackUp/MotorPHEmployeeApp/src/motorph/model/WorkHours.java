package motorph.model;
public class WorkHours {

    private String date;

    private int regularHours;

    private int overtimeHours;

    private String employeeNumber;

    public WorkHours(String date, int regularHours, int overtimeHours, String employeeNumber) {
        this.date = date;
        this.regularHours = regularHours;
        this.overtimeHours = overtimeHours;
        this.employeeNumber = employeeNumber;
    }

    public void recordHours(String date, int regularHours, int overtimeHours) {
        this.date = date;
        this.regularHours = regularHours;
        this.overtimeHours = overtimeHours;
    }

    public int getTotalHours() {
        return regularHours + overtimeHours;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public int getRegularHours() { return regularHours; }
    public void setRegularHours(int regularHours) { this.regularHours = regularHours; }
    
    public int getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(int overtimeHours) { this.overtimeHours = overtimeHours; }
    
    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }
    
    public int getEmployeeId() { return Integer.parseInt(employeeNumber); }

    @Override
    public String toString() {
        return String.format("WorkHours: Emp#%s | %s | %dh regular + %dh OT = %dh total", 
                           employeeNumber, date, regularHours, overtimeHours, getTotalHours());
    }
}