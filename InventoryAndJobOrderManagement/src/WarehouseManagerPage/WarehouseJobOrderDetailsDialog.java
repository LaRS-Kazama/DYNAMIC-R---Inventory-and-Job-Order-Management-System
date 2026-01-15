package WarehouseManagerPage;

import DaoClass.JobOrderDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class WarehouseJobOrderDetailsDialog extends JDialog {

    private JTable table;
    private int rowIndex;

    public WarehouseJobOrderDetailsDialog(JFrame parent, JTable orderTable, int row) {
        super(parent, "Job Order Details", true);

        this.table = orderTable;
        this.rowIndex = row;

        setSize(700, 650);
        setLocationRelativeTo(parent);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBounds(0, 0, 700, 80);
        add(headerPanel);

        JLabel title = new JLabel("JOB ORDER DETAILS");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(30, 15, 400, 35);
        headerPanel.add(title);

        String jobId = table.getValueAt(row, 0).toString();
        JLabel lblJobId = new JLabel("ID: " + jobId);
        lblJobId.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblJobId.setForeground(new Color(52, 152, 219));
        lblJobId.setBounds(30, 50, 300, 25);
        headerPanel.add(lblJobId);

        int y = 100;

        // ===== BASIC INFO =====
        JPanel basicPanel = createSection("Basic Information", 30, y, 640, 140);
        add(basicPanel);

        addDetailRow(basicPanel, "Client / Project:", getTableValue(row, 1), 20, 30);
        addDetailRow(basicPanel, "Assigned Employee:", getTableValue(row, 2), 20, 70);
        addDetailRow(basicPanel, "Deadline:", getTableValue(row, 3), 20, 110);

        y += 160;

        // ===== STATUS =====
        JPanel statusPanel = createSection("Current Status", 30, y, 640, 100);
        add(statusPanel);

        String stage = getTableValue(row, 4);
        String status = getTableValue(row, 5);

        JLabel lblStage = new JLabel("Stage: " + stage);
        lblStage.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblStage.setBounds(20, 30, 300, 25);
        lblStage.setOpaque(true);
        lblStage.setBackground(getStageColor(stage));
        lblStage.setForeground(Color.WHITE);
        lblStage.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.add(lblStage);

        JLabel lblStatus = new JLabel("Status: " + status);
        lblStatus.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblStatus.setBounds(340, 30, 280, 25);
        lblStatus.setOpaque(true);
        lblStatus.setBackground(getStatusColor(status));
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.add(lblStatus);

        y += 120;

        // ===== MATERIALS & NOTES =====
        JPanel materialsPanel = createSection("Materials & Progress", 30, y, 640, 230);
        add(materialsPanel);

        addDetailRow(materialsPanel, "Materials Required:", getTableValue(row, 6), 20, 30);
        addTextAreaRow(materialsPanel, "Progress Notes:", getTableValue(row, 7), 20, 70);

        // ===== CLOSE BUTTON =====
        JButton btnClose = new JButton("Close");
        btnClose.setBounds(270, 570, 160, 40);
        btnClose.setBackground(new Color(149, 165, 166));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnClose.addActionListener(e -> dispose());
        add(btnClose);
    }

    private void addDetailRow(JPanel panel, String label, String value, int x, int y) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblLabel.setBounds(x, y, 180, 25);
        panel.add(lblLabel);

        JLabel lblValue = new JLabel(value != null && !value.isEmpty() ? value : "N/A");
        lblValue.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblValue.setBounds(x + 190, y, 420, 25);
        panel.add(lblValue);
    }

    private void addTextAreaRow(JPanel panel, String label, String value, int x, int y) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblLabel.setBounds(x, y, 180, 20);
        panel.add(lblLabel);

        JTextArea txtValue = new JTextArea(value != null && !value.isEmpty() ? value : "No notes");
        txtValue.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtValue.setLineWrap(true);
        txtValue.setWrapStyleWord(true);
        txtValue.setEditable(false);
        txtValue.setBackground(new Color(245, 245, 245));
        txtValue.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scroll = new JScrollPane(txtValue);
        scroll.setBounds(x, y + 25, 600, 120);
        panel.add(scroll);
    }

    private Color getStageColor(String stage) {
        switch (stage) {
            case "Receiving": return new Color(52, 152, 219);
            case "Inspection": return new Color(230, 126, 34);
            case "Storage": return new Color(241, 196, 15);
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

    private String getTableValue(int row, int col) {
        Object value = table.getValueAt(row, col);
        return value != null ? value.toString() : "";
    }

    private JPanel createSection(String title, int x, int y, int w, int h) {
        JPanel panel = new JPanel(null);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBounds(x, y, w, h);
        panel.setBackground(Color.WHITE);
        return panel;
    }
}