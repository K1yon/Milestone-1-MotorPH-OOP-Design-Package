package motorph.model;
public class User {

    private String employeeNumber;

    private String fullName;

    private String role;

    public User(String employeeNumber, String fullName, String role) {
        this.employeeNumber = employeeNumber;
        this.fullName = fullName;
        this.role = role;
    }

    public boolean isHRAdmin() {
        return "HR".equalsIgnoreCase(role) || "Admin".equalsIgnoreCase(role);
    }

    public boolean isEmployee() {
        return "Employee".equalsIgnoreCase(role);
    }

    public boolean canApproveLeaves() {
        return isHRAdmin();
    }

    public void logout() {
        System.out.println(fullName + " (" + employeeNumber + ") logged out.");
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", fullName, employeeNumber, role);
    }
}