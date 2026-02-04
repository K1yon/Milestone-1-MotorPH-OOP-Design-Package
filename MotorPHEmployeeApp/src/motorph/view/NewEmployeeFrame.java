package motorph.view;

import javax.swing.*;
import java.awt.*;

public class NewEmployeeFrame extends JFrame {

    public NewEmployeeFrame(JFrame parent) {
        setTitle("New Employee");
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(new JLabel("New Employee form placeholder",
                SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
