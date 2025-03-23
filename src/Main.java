//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main extends JFrame {
    private Map<String, String> objectCD; // Maps objects to their Conflict of Interest Class (CD)
    private Map<String, Set<String>> userReadAccess; // Tracks user's read access history

    // GUI Components
    private JTextField userField;
    private JTextField objectField;
    private JTextField cdField;
    private JTextArea outputArea;

    public Main() {
        objectCD = new HashMap<>();
        userReadAccess = new HashMap<>();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Chinese Wall Model Simulation");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create panels
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JPanel buttonPanel = new JPanel(new FlowLayout());
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Input fields
        userField = new JTextField();
        objectField = new JTextField();
        cdField = new JTextField();

        // Add components to input panel
        inputPanel.add(new JLabel("User:"));
        inputPanel.add(userField);
        inputPanel.add(new JLabel("Object:"));
        inputPanel.add(objectField);
        inputPanel.add(new JLabel("Conflict Class (CD):"));
        inputPanel.add(cdField);

        // Buttons
        JButton addObjectButton = new JButton("Add Object");
        JButton readButton = new JButton("Read");
        JButton writeButton = new JButton("Write");

        // Add action listeners
        addObjectButton.addActionListener(e -> addObject());
        readButton.addActionListener(e -> canRead());
        writeButton.addActionListener(e -> canWrite());

        // Add buttons to button panel
        buttonPanel.add(addObjectButton);
        buttonPanel.add(readButton);
        buttonPanel.add(writeButton);

        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void addObject() {
        String objectName = objectField.getText().trim();
        String cdName = cdField.getText().trim();
        if (!objectName.isEmpty() && !cdName.isEmpty()) {
            objectCD.put(objectName, cdName);
            outputArea.append("Added object '" + objectName + "' with conflict class '" + cdName + "'.\n");
        } else {
            outputArea.append("Error: Object name and conflict class cannot be empty.\n");
        }
    }

    private void canRead() {
        String user = userField.getText().trim();
        String object = objectField.getText().trim();
        if (!objectCD.containsKey(object)) {
            outputArea.append("Object '" + object + "' does not exist.\n");
            return;
        }

        if (!userReadAccess.containsKey(user)) {
            userReadAccess.put(user, new HashSet<>());
        }

        String cdOfObject = objectCD.get(object);
        for (String readObject : userReadAccess.get(user)) {
            if (!objectCD.get(readObject).equals(cdOfObject)) {
                outputArea.append("Access Denied: User '" + user + "' has read from conflicting class '" + objectCD.get(readObject) + "'.\n");
                return;
            }
        }

        userReadAccess.get(user).add(object);
        outputArea.append("Access Granted: User '" + user + "' can read '" + object + "'.\n");
    }

    private void canWrite() {
        String user = userField.getText().trim();
        String object = objectField.getText().trim();
        if (canRead(user, object)) {
            outputArea.append("Write Granted: User '" + user + "' can write to '" + object + "'.\n");
        } else {
            outputArea.append("Write Denied: User '" + user + "' cannot write due to read conflicts.\n");
        }
    }

    private boolean canRead(String user, String object) {
        if (!objectCD.containsKey(object)) {
            outputArea.append("Object '" + object + "' does not exist.\n");
            return false;
        }

        if (!userReadAccess.containsKey(user)) {
            userReadAccess.put(user, new HashSet<>());
        }

        String cdOfObject = objectCD.get(object);
        for (String readObject : userReadAccess.get(user)) {
            if (!objectCD.get(readObject).equals(cdOfObject)) {
                outputArea.append("Access Denied: User '" + user + "' has read from conflicting class '" + objectCD.get(readObject) + "'.\n");
                return false;
            }
        }

        userReadAccess.get(user).add(object);
        outputArea.append("Access Granted: User '" + user + "' can read '" + object + "'.\n");
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}
