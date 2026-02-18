package motorph.model;

public class LeaveRequest {
    private String requestId;
    private String employeeId;
    private String leaveType;
    private String status;

    public LeaveRequest(String requestId, String employeeId, String leaveType, String status) {
        this.requestId = requestId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.status = status;
    }

    public String getRequestId()  { return requestId; }
    public String getEmployeeId() { return employeeId; }
    public String getLeaveType()  { return leaveType; }
    public String getStatus()     { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[%s] Employee: %s | Type: %s | Status: %s",
            requestId, employeeId, leaveType, status);
    }
}