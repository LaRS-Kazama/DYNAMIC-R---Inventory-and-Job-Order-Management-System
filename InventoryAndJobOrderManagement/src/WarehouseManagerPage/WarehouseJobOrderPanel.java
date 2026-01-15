package WarehouseManagerPage;

import CommonConstant.AppColors;
import DaoClass.JobOrderDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class WarehouseJobOrderPanel extends JPanel {

    private JTable jobOrderTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbFilterStage, cmbFilterStatus;
    private JTextField txtSearch;

    public WarehouseJobOrderPanel() {
        setLayout(null);
        setBackground(new Color(245, 245, 245));

        // ===== HEADER =====
        JLabel title = new JLabel("WAREHOUSE MANAGER - JOB ORDERS");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(50, 50, 50));
        title.setBounds(50, 25, 500, 40);
        add(title);

        // ===== QUICK STATS =====
        createQuickStats();

        // ===== FILTERS =====
        JPanel filterPanel = new JPanel(null);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Job Orders"));
        filterPanel.setBounds(50, 180, 1100, 70);
        add(filterPanel);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setBounds(20, 25, 80, 30);
        filterPanel.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(100, 25, 200, 30);
        filterPanel.add(txtSearch);

        JLabel lblStage = new JLabel("Stage:");
        lblStage.setBounds(330, 25, 60, 30);
        filterPanel.add(lblStage);

        cmbFilterStage = new JComboBox<>(new String[]{
                "All", "Receiving", "Inspection", "Storage", "Completed"
        });
        cmbFilterStage.setBounds(400, 25, 150, 30);
        filterPanel.add(cmbFilterStage);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(580, 25, 60, 30);
        filterPanel.add(lblStatus);

        cmbFilterStatus = new JComboBox<>(new String[]{
                "All", "In Progress", "Pending", "On Hold", "Completed"
        });
        cmbFilterStatus.setBounds(650, 25, 150, 30);
        filterPanel.add(cmbFilterStatus);

        JButton btnFilter = new JButton("Apply");
        btnFilter.setBounds(830, 25, 100, 30);
        btnFilter.setBackground(AppColors.PRIMARY_RED);
        btnFilter.setForeground(Color.WHITE);
        btnFilter.addActionListener(e -> applyFilters());
        filterPanel.add(btnFilter);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(950, 25, 100, 30);
        btnClear.setBackground(Color.GRAY);
        btnClear.setForeground(Color.WHITE);
        btnClear.addActionListener(e -> clearFilters());
        filterPanel.add(btnClear);

        // ===== ACTION BUTTONS =====
        JPanel actionPanel = new JPanel(null);
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBounds(50, 270, 1100, 60);
        add(actionPanel);

        JButton btnViewDetails = new JButton("View Details");
        btnViewDetails.setBounds(20, 10, 150, 40);
        btnViewDetails.setBackground(new Color(52, 152, 219));
        btnViewDetails.setForeground(Color.WHITE);
        btnViewDetails.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnViewDetails.addActionListener(e -> viewJobOrderDetails());
        actionPanel.add(btnViewDetails);

        JButton btnUpdateProgress = new JButton("Update Progress");
        btnUpdateProgress.setBounds(190, 10, 170, 40);
        btnUpdateProgress.setBackground(new Color(46, 204, 113));
        btnUpdateProgress.setForeground(Color.WHITE);
        btnUpdateProgress.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnUpdateProgress.addActionListener(e -> updateProgress());
        actionPanel.add(btnUpdateProgress);

        JButton btnRefresh = new JButton("↻ Refresh");
        btnRefresh.setBounds(380, 10, 120, 40);
        btnRefresh.setBackground(new Color(149, 165, 166));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> loadJobOrders());
        actionPanel.add(btnRefresh);

        // ===== TABLE =====
        createJobOrderTable();

        JScrollPane scrollPane = new JScrollPane(jobOrderTable);
        scrollPane.setBounds(50, 350, 1100, 330);
        add(scrollPane);

        // Load data
        loadJobOrders();
    }


    private void createQuickStats() {
        // Active Jobs = In Progress status
        int activeJobs = JobOrderDao.getJobOrdersByDepartment("Mechanical", "In Progress");

        // Pending Jobs = Pending status
        int pendingJobs = JobOrderDao.getJobOrdersByDepartment("Mechanical", "Pending");

        // Completed Jobs = Completed stage (not status)
        int completedJobs = JobOrderDao.getJobOrdersByDepartmentAndStage("Mechanical", "Completed");

        // Total = count all job orders for Electrical department
        int totalJobs = activeJobs + pendingJobs + completedJobs +
                JobOrderDao.getJobOrdersByDepartment("Mechanical", "On Hold");

        JPanel cardActive = createStatCard("Active Jobs", activeJobs,
                new Color(52, 152, 219));
        cardActive.setBounds(50, 80, 250, 80);
        add(cardActive);

        JPanel cardPending = createStatCard("Pending Jobs", pendingJobs,
                new Color(241, 196, 15));
        cardPending.setBounds(330, 80, 250, 80);
        add(cardPending);

        JPanel cardCompleted = createStatCard("Completed", completedJobs,
                new Color(46, 204, 113));
        cardCompleted.setBounds(610, 80, 250, 80);
        add(cardCompleted);

        JPanel cardTotal = createStatCard("Total Jobs", totalJobs,
                new Color(52, 73, 94));
        cardTotal.setBounds(890, 80, 250, 80);
        add(cardTotal);
    }

    private JPanel createStatCard(String title, int count, Color color) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblTitle.setForeground(Color.DARK_GRAY);
        lblTitle.setBounds(15, 15, 200, 20);
        card.add(lblTitle);

        JLabel lblCount = new JLabel(String.valueOf(count));
        lblCount.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblCount.setForeground(color);
        lblCount.setBounds(15, 35, 200, 30);
        card.add(lblCount);

        return card;
    }

    private void createJobOrderTable() {
        String[] columns = {
                "Job ID", "Client Name", "Employee Assigned", "Deadline",
                "Stage", "Status", "Materials", "Progress Notes"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jobOrderTable = new JTable(tableModel);
        jobOrderTable.setRowHeight(30);
        jobOrderTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        jobOrderTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        jobOrderTable.getTableHeader().setBackground(new Color(52, 73, 94));
        jobOrderTable.getTableHeader().setForeground(Color.WHITE);
        jobOrderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Column widths
        jobOrderTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        jobOrderTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        jobOrderTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        jobOrderTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        jobOrderTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        jobOrderTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        jobOrderTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        jobOrderTable.getColumnModel().getColumn(7).setPreferredWidth(200);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        jobOrderTable.setRowSorter(sorter);

        jobOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewJobOrderDetails();
                }
            }
        });
    }

    // Replace the loadJobOrders() method in WarehouseJobOrderPanel.java with this:

    private void loadJobOrders() {
        try {
            List<Object[]> rows = JobOrderDao.getJobOrdersByDepartment("Mechanical");
            tableModel.setRowCount(0);

            for (Object[] row : rows) {
                // DAO returns: [job_id, client_name, employee_assigned, deadline, stage, status, materials, notes]
                // Table expects: [Job ID, Client Name, Employee Assigned, Deadline, Stage, Status, Materials, Progress Notes]

                tableModel.addRow(new Object[]{
                        row[0],     // Job ID
                        row[1],     // Client Name
                        row[2],     // Employee Assigned
                        row[3],     // Deadline
                        row[4],     // Stage
                        row[5],     // Status
                        row[6],     // Materials
                        row[7]      // Progress Notes
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading job orders: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilters() {
        String searchText = txtSearch.getText().toLowerCase().trim();
        String selectedStage = cmbFilterStage.getSelectedItem().toString();
        String selectedStatus = cmbFilterStatus.getSelectedItem().toString();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        jobOrderTable.setRowSorter(sorter);

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                if (!searchText.isEmpty()) {
                    boolean matchFound = false;
                    for (int i = 0; i < entry.getValueCount(); i++) {
                        String value = entry.getStringValue(i).toLowerCase();
                        if (value.contains(searchText)) {
                            matchFound = true;
                            break;
                        }
                    }
                    if (!matchFound) return false;
                }

                if (!selectedStage.equals("All")) {
                    String stage = entry.getStringValue(4);
                    if (!stage.equals(selectedStage)) return false;
                }

                if (!selectedStatus.equals("All")) {
                    String status = entry.getStringValue(5);
                    if (!status.equals(selectedStatus)) return false;
                }

                return true;
            }
        });
    }

    private void clearFilters() {
        txtSearch.setText("");
        cmbFilterStage.setSelectedIndex(0);
        cmbFilterStatus.setSelectedIndex(0);
        jobOrderTable.setRowSorter(new TableRowSorter<>(tableModel));
    }

    private void viewJobOrderDetails() {
        int selectedRow = jobOrderTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a job order to view.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = jobOrderTable.convertRowIndexToModel(selectedRow);

        WarehouseJobOrderDetailsDialog dialog = new WarehouseJobOrderDetailsDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                jobOrderTable,
                modelRow
        );
        dialog.setVisible(true);
    }

    private void updateProgress() {
        int selectedRow = jobOrderTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a job order to update.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = jobOrderTable.convertRowIndexToModel(selectedRow);

        WarehousePage.WarehouseUpdateProgressDialog dialog = new WarehousePage.WarehouseUpdateProgressDialog( // FIXED: Use warehouse-specific dialog
                (JFrame) SwingUtilities.getWindowAncestor(this),
                jobOrderTable,
                modelRow
        );
        dialog.setVisible(true);
        loadJobOrders();
    }
}