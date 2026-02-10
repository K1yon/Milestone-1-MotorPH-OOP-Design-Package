package motorph.controller;

import javax.swing.JOptionPane;
import java.io.IOException;

public class LoginController {
    private AuthService authenticator; 
    private int attemptCount = 0;      
    private final int maxAttempts = 3;  

    public LoginController() {
        try {
            authenticator = new Authenticator("users.csv");
            System.out.println("Loaded " + ((Authenticator)authenticator).getUserCount() + " users from users.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Failed to load user credentials from users.csv. Application will exit.",
                "Critical Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public boolean handleLogin(String employeeNumber, String password) {

        if (attemptCount >= maxAttempts) {
            System.err.println("Maximum login attempts exceeded. Account locked.");
            return false;
        }

        attemptCount++;
        System.out.println("[DEBUG] Attempt #" + attemptCount + ": EmployeeNumber='" + 
                          employeeNumber + "'");
        
        boolean authenticated = authenticator.authenticate(employeeNumber, password);
        
        if (!authenticated) {
            System.err.println("Login attempt " + attemptCount + "/" + maxAttempts + " failed.");
            if (attemptCount >= maxAttempts) {
                System.err.println("Account locked after " + maxAttempts + " failed attempts.");
            }
        }
        
        return authenticated;
    }

    public boolean isValidEmployeeNumber(String employeeNumber) {
        return ((Authenticator)authenticator).userExists(employeeNumber);
    }

    public void resetAttempts() {
        attemptCount = 0;
    }

    public int getAttemptCount() { return attemptCount; }
    public int getMaxAttempts() { return maxAttempts; }
    public boolean isLocked() { return attemptCount >= maxAttempts; }

public boolean isHRAdmin() {
    String currentUser = UserSession.getCurrentUser();
    return currentUser != null && currentUser.toUpperCase().startsWith("HR");
}

}