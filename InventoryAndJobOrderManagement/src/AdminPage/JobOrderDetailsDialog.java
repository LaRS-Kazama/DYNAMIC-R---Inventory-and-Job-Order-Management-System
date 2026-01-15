package AdminPage;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class JobOrderDetailsDialog extends JDialog {

    private JTable table;
    private int rowIndex;

    public JobOrderDetailsDialog(JFrame parent, JTable orderTable, int row) {
        super(parent, "Job Order Details", true);

        this.table = orderTable;
        this.rowIndex = row;

        setSize(800, 750);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBounds(0, 0, 800, 80);
        mainPanel.add(headerPanel);

        JLabel title = new JLabel("JOB ORDER DETAILS");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBounds(30, 15, 400, 35);
        headerPanel.add(title);

        String jobId = getTableValue(row, 1);
        JLabel lblJobId = new JLabel("ID: " + jobId);
        lblJobId.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblJobId.setForeground(new Color(52, 152, 219));
        lblJobId.setBounds(30, 50, 300, 25);
        headerPanel.add(lblJobId);

        String dateCreated = getTableValue(row, 14);
        JLabel lblDateCreated = new JLabel("Created: " + dateCreated);
        lblDateCreated.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDateCreated.setForeground(Color.WHITE);
        lblDateCreated.setBounds(600, 50, 180, 25);
        headerPanel.add(lblDateCreated);

        int y = 100;

        // ===== BASIC INFORMATION SECTION =====
        JPanel basicPanel = createSection("Basic Information", 30, y, 740, 180);
        mainPanel.add(basicPanel);

        addDetailRow(basicPanel, "Client / Project:", getTableValue(row, 2), 20, 30);
        addDetailRow(basicPanel, "Department:", getTableValue(row, 3), 20, 70);
        addDetailRow(basicPanel, "Assigned Employee:", getTableValue(row, 4), 20, 110);

        y += 200;

        // ===== TIMELINE SECTION =====
        JPanel timelinePanel = createSection("Timeline & Status", 30, y, 740, 140);
        mainPanel.add(timelinePanel);

        addDetailRow(timelinePanel, "Deadline:", getTableValue(row, 5), 20, 30);

        // Stage with color coding
        String stage = getTableValue(row, 6);
        JLabel lblStageLabel = new JLabel("Current Stage:");
        lblStageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblStageLabel.setBounds(20, 70, 180, 25);
        timelinePanel.add(lblStageLabel);

        JLabel lblStageValue = new JLabel(stage);
        lblStageValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblStageValue.setBounds(210, 70, 500, 25);
        lblStageValue.setOpaque(true);
        lblStageValue.setForeground(Color.WHITE);
        lblStageValue.setBackground(getStageColor(stage));
        lblStageValue.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        timelinePanel.add(lblStageValue);

        // Status with color coding
        String status = getTableValue(row, 7);
        JLabel lblStatusLabel = new JLabel("Status:");
        lblStatusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblStatusLabel.setBounds(400, 70, 100, 25);
        timelinePanel.add(lblStatusLabel);

        JLabel lblStatusValue = new JLabel(status);
        lblStatusValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblStatusValue.setBounds(510, 70, 200, 25);
        lblStatusValue.setOpaque(true);
        lblStatusValue.setForeground(Color.WHITE);
        lblStatusValue.setBackground(getStatusColor(status));
        lblStatusValue.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        timelinePanel.add(lblStatusValue);

        y += 160;

        // ===== JOB DETAILS SECTION =====
        JPanel detailsPanel = createSection("Job Scope & Materials", 30, y, 740, 220);
        mainPanel.add(detailsPanel);

        addDetailRow(detailsPanel, "Materials Required:", getTableValue(row, 8), 20, 30);
        addTextAreaRow(detailsPanel, "Job Description:", getTableValue(row, 9), 20, 70);
        addTextAreaRow(detailsPanel, "Customization:", getTableValue(row, 10), 400, 70);

        y += 240;

        // ===== NOTES & REFERENCES SECTION =====
        JPanel notesPanel = createSection("Notes & References", 30, y, 740, 180);
        mainPanel.add(notesPanel);

        addTextAreaRow(notesPanel, "Progress Notes:", getTableValue(row, 11), 20, 30);
        addDetailRow(notesPanel, "Reference No.:", getTableValue(row, 12), 20, 110);

        // Attachment with open button
        String attachmentPath = getTableValue(row, 13);
        JLabel lblAttachLabel = new JLabel("Attachment:");
        lblAttachLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblAttachLabel.setBounds(20, 145, 180, 25);
        notesPanel.add(lblAttachLabel);

        if (attachmentPath != null && !attachmentPath.isEmpty()) {
            String fileName = new File(attachmentPath).getName();
            JLabel lblAttachment = new JLabel(fileName);
            lblAttachment.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblAttachment.setBounds(210, 145, 350, 25);
            lblAttachment.setForeground(new Color(52, 152, 219));
            notesPanel.add(lblAttachment);

            JButton btnOpenFile = new JButton("Open File");
            btnOpenFile.setBounds(570, 143, 120, 30);
            btnOpenFile.setBackground(new Color(52, 152, 219));
            btnOpenFile.setForeground(Color.WHITE);
            btnOpenFile.addActionListener(e -> openAttachment(attachmentPath));
            notesPanel.add(btnOpenFile);
        } else {
            JLabel lblNoAttachment = new JLabel("No attachment");
            lblNoAttachment.setFont(new Font("SansSerif", Font.ITALIC, 13));
            lblNoAttachment.setForeground(Color.GRAY);
            lblNoAttachment.setBounds(210, 145, 500, 25);
            notesPanel.add(lblNoAttachment);
        }

        // ===== CLOSE BUTTON =====
        JButton btnClose = new JButton("Close");
        btnClose.setBounds(320, 680, 160, 40);
        btnClose.setBackground(new Color(149, 165, 166));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnClose.addActionListener(e -> dispose());
        mainPanel.add(btnClose);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);
    }

    private void addDetailRow(JPanel panel, String label, String value, int x, int y) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblLabel.setBounds(x, y, 180, 25);
        panel.add(lblLabel);

        JLabel lblValue = new JLabel(value != null && !value.isEmpty() ? value : "N/A");
        lblValue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblValue.setBounds(x + 190, y, 500, 25);
        panel.add(lblValue);
    }

    private void addTextAreaRow(JPanel panel, String label, String value, int x, int y) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblLabel.setBounds(x, y, 180, 20);
        panel.add(lblLabel);

        JTextArea txtValue = new JTextArea(value != null && !value.isEmpty() ? value : "N/A");
        txtValue.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtValue.setLineWrap(true);
        txtValue.setWrapStyleWord(true);
        txtValue.setEditable(false);
        txtValue.setBackground(new Color(245, 245, 245));
        txtValue.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scroll = new JScrollPane(txtValue);
        scroll.setBounds(x, y + 25, 330, 70);
        panel.add(scroll);
    }

    private Color getStageColor(String stage) {
        switch (stage) {
            case "Receiving": return new Color(52, 152, 219);
            case "Dismantling": return new Color(230, 126, 34);
            case "Rebuilding": return new Color(241, 196, 15);
            case "Painting": return new Color(155, 89, 182);
            case "Inspection": return new Color(52, 73, 94);
            case "Completed": return new Color(46, 204, 113);
            default: return Color.GRAY;
        }
    }

    private Color getStatusColor(String status) {
        switch (status) {
            case "In Progress": return new Color(52, 152, 219);
            case "Pending": return new Color(241, 196, 15);
            case "On Hold": return new Color(230, 126, 34);
            case "Completed": return new Color(46, 204, 113);
            default: return Color.GRAY;
        }
    }

    private void openAttachment(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(this,
                        "File not found: " + path,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error opening file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getTableValue(int row, int col) {
        Object value = table.getValueAt(row, col);
        return value != null ? value.toString() : "";
    }

    private JPanel createSection(String title, int x, int y, int w, int h) {
        JPanel panel = new JPanel(null);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                title,
                0,
                0,
                new Font("SansSerif", Font.BOLD, 15),
                new Color(52, 73, 94)
        ));
        panel.setBounds(x, y, w, h);
        panel.setBackground(Color.WHITE);
        return panel;
    }
}