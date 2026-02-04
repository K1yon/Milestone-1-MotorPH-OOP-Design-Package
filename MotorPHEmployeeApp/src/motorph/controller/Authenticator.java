package motorph.controller;

import java.io.*;
import java.util.*;

public class Authenticator {
    private Map<String, String> credentials = new HashMap<>();

    // Load credentials from users.csv
    public Authenticator(String usersFilePath) throws IOException {
        loadCredentials(usersFilePath);
    }

    private void loadCredentials(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    String empNum = parts[0].trim();
                    String password = parts[1].trim();
                    credentials.put(empNum, password);
                }
            }
        }
    }

    // Validate login credentials
    public boolean authenticate(String employeeNumber, String password) {
        return password.equals(credentials.get(employeeNumber));
    }
}
