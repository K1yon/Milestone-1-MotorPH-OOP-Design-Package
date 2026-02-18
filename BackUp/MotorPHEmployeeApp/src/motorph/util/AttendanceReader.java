package motorph.util;

import motorph.model.WorkHours;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AttendanceReader {
    private static final String CSV_FILE = "attendance_record.csv";
    private List<WorkHours> allAttendance;

    public AttendanceReader() {
        allAttendance = new ArrayList<>();
        loadAttendanceData();
    }

    private void loadAttendanceData() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean isFirstLine = true;
            int recordsLoaded = 0;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; 
                }
                
                try {
                    String[] values = line.split(",");
                    
                    if (values.length >= 6) {
                        String employeeNumber = values[0].trim();
                        String date = values[3].trim();
                        String logIn = values[4].trim();
                        String logOut = values[5].trim();
                        
                        double hoursWorked = calculateHoursWorked(logIn, logOut);
                        
                        int regularHours = (int) Math.min(hoursWorked, 8);
                        
                        int overtimeHours = (int) Math.max(hoursWorked - 8, 0);
                        
                        WorkHours wh = new WorkHours(date, regularHours, overtimeHours, employeeNumber);
                        allAttendance.add(wh);
                        recordsLoaded++;
                    }
                } catch (Exception e) {

                    continue;
                }
            }
            System.out.println("Loaded " + recordsLoaded + " attendance records");
        } catch (IOException e) {
            System.err.println("Error loading attendance data: " + e.getMessage());
        }
    }
    
    private double calculateHoursWorked(String logIn, String logOut) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
            LocalTime timeIn = LocalTime.parse(logIn, formatter);
            LocalTime timeOut = LocalTime.parse(logOut, formatter);
            
            // Calculate hours between login and logout
            long minutes = timeIn.until(timeOut, ChronoUnit.MINUTES);
            
            minutes -= 60;
            
            return Math.max(minutes / 60.0, 0);
        } catch (Exception e) {
            return 0; 
        }
    }

    public List<WorkHours> getAttendanceForMonth(String employeeNumber, int month) {
        List<WorkHours> monthlyRecords = new ArrayList<>();
        
        for (WorkHours wh : allAttendance) {
            if (wh.getEmployeeNumber().equals(employeeNumber)) {
     
                String[] dateParts = wh.getDate().split("/");
                if (dateParts.length == 3) {
                    int recordMonth = Integer.parseInt(dateParts[0]);
                    if (recordMonth == month) {
                        monthlyRecords.add(wh);
                    }
                }
            }
        }
        
        return monthlyRecords;
    }

    public List<WorkHours> getAllAttendanceForEmployee(String employeeNumber) {
        List<WorkHours> employeeRecords = new ArrayList<>();
        
        for (WorkHours wh : allAttendance) {
            if (wh.getEmployeeNumber().equals(employeeNumber)) {
                employeeRecords.add(wh);
            }
        }
        
        return employeeRecords;
    }
}