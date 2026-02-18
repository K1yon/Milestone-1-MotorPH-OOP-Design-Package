package motorph.controller;

public class UserSession {
    private static String currentEmployeeNumber;
    private static String currentRole = "Employee";

    public static void setCurrentUser(String empNum) {
        currentEmployeeNumber = empNum;
        currentRole = empNum.toUpperCase().startsWith("HR") ? "HR" : "Employee";
    }
    
    public static void setCurrentUser(String empNum, String role) {
        currentEmployeeNumber = empNum;
        currentRole = role;
    }

    public static String getCurrentUser() { return currentEmployeeNumber; }
    public static String getCurrentRole() { return currentRole; }
    public static boolean isHRAdmin() { return "HR".equalsIgnoreCase(currentRole); }
}