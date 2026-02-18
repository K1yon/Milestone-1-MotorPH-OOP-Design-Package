package motorph.view;

import motorph.model.LeaveRequest;
import motorph.service.LeaveService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class HRLeaveFrame extends JFrame {

    private LeaveService service = new LeaveService();
    private JList<String> list;
    private DefaultListModel<String> model;
    private List<LeaveRequest> requests;

    public HRLeaveFrame(JFrame parent) {
        setTitle("Leave Requests");
        setSize(500, 400);
        setLocationRelativeTo(parent);

        model = new DefaultListModel<>();
        list = new JList<>(model);

        JButton approve = new JButton("Approve");
        JButton deny = new JButton("Deny");

        approve.addActionListener(e -> update("APPROVED"));
        deny.addActionListener(e -> update("DENIED"));

        JPanel btnPanel = new JPanel();
        btnPanel.add(approve);
        btnPanel.add(deny);

        add(new JScrollPane(list), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        refresh();
        setVisible(true);
    }

    private void refresh() {
        model.clear();
        try {
            requests = service.getAllRequests();
            for (LeaveRequest r : requests) {
                model.addElement(r.toString());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                this,
                "Failed to load leave requests.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void update(String status) {
        int index = list.getSelectedIndex();
        if (index >= 0) {
            try {
                LeaveRequest req = requests.get(index);
                service.updateStatus(req.getRequestId(), status);
                refresh();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to update leave request.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}