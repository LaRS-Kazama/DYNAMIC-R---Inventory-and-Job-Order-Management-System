package AdminPage;

import CommonConstant.AppColors;
import DaoClass.JobOrderDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteJobOrderDialog extends JDialog {

    private JTable table;
    private List<Integer> selectedRows;

    public DeleteJobOrderDialog(JFrame parent, JTable orderTable) {
        super(parent, "Delete Job Order(s)", true);

        this.table = orderTable;
        this.selectedRows = new ArrayList<>();

        // Get selected rows
        for (int i = 0; i < table.getRowCount(); i++) {
            Boolean checked = (Boolean) table.getValueAt(i, 0);
            if (checked != null && checked) {
                selectedRows.add(i);
            }
        }

        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                    "Please select at least one job order to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        }

        setSize(500, 350);
        setLocationRelativeTo(parent);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Confirm Deletion", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(220, 53, 69));
        title.setBounds(0, 20, 500, 30);
        add(title);

        // Warning icon
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
        iconLabel.setBounds(220, 60, 60, 60);
        add(iconLabel);

        // Message
        JLabel message = new JLabel("<html><center>Are you sure you want to delete " +
                selectedRows.size() + " job order(s)?<br><br>" +
                "This action cannot be undone!</center></html>", SwingConstants.CENTER);
        message.setFont(new Font("SansSerif", Font.PLAIN, 14));
        message.setBounds(50, 130, 400, 60);
        add(message);

        // Job Order Details Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(248, 249, 250));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Job Orders to Delete:"));

        for (int row : selectedRows) {
            String jobId = table.getValueAt(row, 1).toString();
            String clientName = table.getValueAt(row, 2).toString();
            String department = table.getValueAt(row, 3).toString();

            JLabel jobLabel = new JLabel("  • " + jobId + " - " + clientName + " (" + department + ")");
            jobLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            detailsPanel.add(jobLabel);
        }

        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        scrollPane.setBounds(50, 200, 400, 80);
        add(scrollPane);

        // Buttons
        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(120, 290, 120, 40);
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> performDelete());
        add(btnDelete);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(260, 290, 120, 40);
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);
    }

    private void performDelete() {
        try {
            int successCount = 0;
            int failCount = 0;
            List<String> failedIds = new ArrayList<>();

            // Delete in reverse order to avoid index shifting issues
            for (int i = selectedRows.size() - 1; i >= 0; i--) {
                int row = selectedRows.get(i);
                String jobId = table.getValueAt(row, 1).toString();

                boolean deleted = JobOrderDao.deleteJobOrder(jobId);

                if (deleted) {
                    successCount++;
                } else {
                    failCount++;
                    failedIds.add(jobId);
                }
            }

            // Refresh table
            refreshJobOrderTable();

            // Show result message
            if (failCount == 0) {
                JOptionPane.showMessageDialog(this,
                        successCount + " job order(s) deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Deleted: " + successCount + "\nFailed: " + failCount +
                                "\n\nFailed IDs: " + String.join(", ", failedIds),
                        "Partial Success",
                        JOptionPane.WARNING_MESSAGE);
            }

            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error during deletion: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshJobOrderTable() {
        try {
            List<Object[]> rows = JobOrderDao.getAllJobOrders();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            for (Object[] row : rows) {
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}