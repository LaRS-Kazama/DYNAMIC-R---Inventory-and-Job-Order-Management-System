package WarehousePage;

import CommonConstant.AppColors;
import DaoClass.JobOrderDao;

import javax.swing.*;
import java.awt.*;

public class WarehouseUpdateProgressDialog extends JDialog {

    private JTable table;
    private int rowIndex;
    private JComboBox<String> cmbStage, cmbStatus;
    private JTextArea txtNotes;

    public WarehouseUpdateProgressDialog(JFrame parent, JTable orderTable, int row) {
        super(parent, "Update Job Order Progress", true);

        this.table = orderTable;
        this.rowIndex = row;

        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBounds(0, 0, 600, 70);
        add(headerPanel);

        JLabel title = new JLabel("UPDATE PROGRESS");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBounds(30, 15, 400, 30);
        headerPanel.add(title);

        String jobId = table.getValueAt(row, 0).toString();
        JLabel lblJobId = new JLabel("Job ID: " + jobId);
        lblJobId.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblJobId.setForeground(Color.WHITE);
        lblJobId.setBounds(30, 45, 400, 20);
        headerPanel.add(lblJobId);

        // ===== JOB INFO =====
        JPanel infoPanel = new JPanel(null);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Current Job Information"));
        infoPanel.setBounds(30, 90, 540, 100);
        infoPanel.setBackground(new Color(245, 245, 245));
        add(infoPanel);

        String client = table.getValueAt(row, 1).toString();
        String employee = table.getValueAt(row, 2).toString();

        JLabel lblClient = new JLabel("Client: " + client);
        lblClient.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblClient.setBounds(20, 30, 500, 25);
        infoPanel.add(lblClient);

        JLabel lblEmployee = new JLabel("Assigned To: " + employee);
        lblEmployee.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblEmployee.setBounds(20, 60, 500, 25);
        infoPanel.add(lblEmployee);

        // ===== UPDATE FIELDS =====
        int y = 210;

        JLabel lblStage = new JLabel("Current Stage:");
        lblStage.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblStage.setBounds(50, y, 150, 25);
        add(lblStage);

        cmbStage = new JComboBox<>(new String[]{
                "Receiving", "Inspection", "Storage", "Completed"
        });
        cmbStage.setBounds(200, y, 320, 30);
        cmbStage.setSelectedItem(table.getValueAt(row, 4).toString());
        add(cmbStage);

        y += 45;

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblStatus.setBounds(50, y, 150, 25);
        add(lblStatus);

        cmbStatus = new JComboBox<>(new String[]{
                "In Progress", "Pending", "On Hold", "Completed"
        });
        cmbStatus.setBounds(200, y, 320, 30);
        cmbStatus.setSelectedItem(table.getValueAt(row, 5).toString());
        add(cmbStatus);

        y += 45;

        JLabel lblNotes = new JLabel("Progress Notes:");
        lblNotes.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblNotes.setBounds(50, y, 150, 25);
        add(lblNotes);

        // Column 7 is the notes column for warehouse (includes materials column)
        Object notesValue = table.getValueAt(row, 7);
        txtNotes = new JTextArea(notesValue != null ? notesValue.toString() : "");
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        txtNotes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtNotes.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JScrollPane scrollNotes = new JScrollPane(txtNotes);
        scrollNotes.setBounds(50, y + 30, 500, 100);
        add(scrollNotes);

        // ===== BUTTONS =====
        JButton btnSave = new JButton("Save Changes");
        btnSave.setBounds(150, 430, 140, 40);
        btnSave.setBackground(AppColors.PRIMARY_RED);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnSave.addActionListener(e -> saveProgress());
        add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(310, 430, 140, 40);
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);
    }

    private void saveProgress() {
        try {
            String jobId = table.getValueAt(rowIndex, 0).toString();
            String stage = cmbStage.getSelectedItem().toString();
            String status = cmbStatus.getSelectedItem().toString();
            String notes = txtNotes.getText().trim();

            // FIXED: Use the correct method with proper signature
            boolean success = JobOrderDao.updateJobOrderProgress(jobId, stage, status, notes);

            if (!success) {
                JOptionPane.showMessageDialog(this,
                        "Failed to update progress.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update table visually
            table.setValueAt(stage, rowIndex, 4);
            table.setValueAt(status, rowIndex, 5);
            table.setValueAt(notes, rowIndex, 7); // Column 7 for warehouse

            JOptionPane.showMessageDialog(this,
                    "Progress updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}