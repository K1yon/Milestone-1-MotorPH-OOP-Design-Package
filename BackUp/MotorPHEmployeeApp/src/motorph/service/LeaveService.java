package motorph.service;

import motorph.dao.LeaveRequestDAO;
import motorph.model.LeaveRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LeaveService {
    private final LeaveRequestDAO dao = new LeaveRequestDAO();

    public void submitRequest(String employeeId, String leaveType) throws IOException {
        LeaveRequest request = new LeaveRequest(
            UUID.randomUUID().toString(),
            employeeId,
            leaveType,
            "PENDING"
        );
        dao.save(request);
    }

    public void submitRequest(String employeeId,
                              String leaveType,
                              String startDate,
                              String endDate) throws IOException {
        LeaveRequest request = new LeaveRequest(
            UUID.randomUUID().toString(),
            employeeId,
            leaveType + " (" + startDate + " to " + endDate + ")",
            "PENDING"
        );
        dao.save(request);
    }

    public List<LeaveRequest> getAllRequests() throws IOException {
        return dao.findAll();
    }

    public List<LeaveRequest> getRequestsByEmployee(String employeeId) throws IOException {
        return dao.findAll().stream()
            .filter(r -> r.getEmployeeId().equals(employeeId))
            .collect(Collectors.toList());
    }

    public void updateStatus(String requestId, String status) throws IOException {
        dao.updateStatus(requestId, status);
    }
}