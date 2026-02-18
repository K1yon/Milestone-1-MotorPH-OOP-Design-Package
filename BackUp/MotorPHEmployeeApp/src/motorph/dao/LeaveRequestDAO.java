package motorph.dao;

import motorph.model.LeaveRequest;
import java.io.*;
import java.util.*;

public class LeaveRequestDAO {

    private static final String FILE = "leave_requests.csv";

    public void save(LeaveRequest request) throws IOException {
        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write(
                request.getRequestId() + "," +
                request.getEmployeeId() + "," +
                request.getLeaveType() + "," +
                request.getStatus() + "\n"
            );
        }
    }

    public List<LeaveRequest> findAll() throws IOException {
        List<LeaveRequest> list = new ArrayList<>();
        File file = new File(FILE);

        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                list.add(new LeaveRequest(d[0], d[1], d[2], d[3]));
            }
        }
        return list;
    }

    public void updateStatus(String requestId, String status) throws IOException {
        File input = new File(FILE);
        File temp = new File("leave_temp.csv");

        try (
            BufferedReader br = new BufferedReader(new FileReader(input));
            PrintWriter pw = new PrintWriter(new FileWriter(temp))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].equals(requestId)) {
                    d[3] = status;
                }
                pw.println(String.join(",", d));
            }
        }

        input.delete();
        temp.renameTo(input);
    }
}