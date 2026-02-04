package motorph.model;

/**
 * Represents the work hours logged by an employee on a specific date.
 * <p>
 * This class encapsulates the date, hours worked, and employee ID,
 * and provides methods to record or update work hours.
 * </p>
 */
public class WorkHours {

    /** Date of the work entry (e.g., "2025-05-20"). */
    private String date;

    /** Number of hours worked on the date. */
    private int hoursWorked;

    /** ID of the employee who logged the hours. */
    private int employeeId;

    /**
     * Constructs a WorkHours entry with specified details.
     *
     * @param date Date of work hours logged.
     * @param hoursWorked Number of hours worked.
     * @param employeeId ID of the employee.
     */
    public WorkHours(String date, int hoursWorked, int employeeId) {
        this.date = date;
        this.hoursWorked = hoursWorked;
        this.employeeId = employeeId;
    }

    /**
     * Records or updates the number of hours worked on a given date.
     *
     * @param date Date for which to record hours.
     * @param hours Number of hours worked.
     */
    public void recordHours(String date, int hours) {
        this.date = date;
        this.hoursWorked = hours;
        // Additional logic to save or update record can be added here
    }

    // Getters and setters for encapsulation

    public String getDate() {
        return date;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}