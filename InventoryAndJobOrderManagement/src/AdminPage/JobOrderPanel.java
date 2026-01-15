package AdminPage;

import CommonConstant.AppColors;
import DaoClass.JobOrderDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class JobOrderPanel extends JPanel {

    private JTable jobOrderTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbFilterStage, cmbFilterStatus;

    public JobOrderPanel() {
        setLayout(null);
        setBackground(new Color(245, 245, 245));

        // ===== HEADER SECTION =====
        JLabel title = new JLabel("JOB ORDER MANAGEMENT");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(50, 50, 50));
        title.setBounds(50, 25, 500, 40);
        add(title);

        // ===== SEARCH AND FILTER SECTION =====
        JPanel filterPanel = new JPanel(null);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));
        filterPanel.setBounds(50, 80, 1300, 80);
        add(filterPanel);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setBounds(20, 30, 80, 30);
        filterPanel.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(100, 30, 250, 30);
        filterPanel.add(txtSearch);

        JLabel lblStage = new JLabel("Stage:");
        lblStage.setBounds(380, 30, 80, 30);
        filterPanel.add(lblStage);

        cmbFilterStage = new JComboBox<>(new String[]{
                "All", "Receiving", "Dismantling", "Rebuilding", "Painting", "Inspection", "Completed"
        });
        cmbFilterStage.setBounds(450, 30, 180, 30);
        filterPanel.add(cmbFilterStage);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(660, 30, 80, 30);
        filterPanel.add(lblStatus);

        cmbFilterStatus = new JComboBox<>(new String[]{
                "All", "In Progress", "Pending", "On Hold", "Completed"
        });
        cmbFilterStatus.setBounds(730, 30, 180, 30);
        filterPanel.add(cmbFilterStatus);

        JButton btnApplyFilter = new JButton("Apply Filter");
        btnApplyFilter.setBounds(940, 30, 130, 30);
        btnApplyFilter.setBackground(AppColors.PRIMARY_RED);
        btnApplyFilter.setForeground(Color.WHITE);
        btnApplyFilter.addActionListener(e -> applyFilters());
        filterPanel.add(btnApplyFilter);

        JButton btnClearFilter = new JButton("Clear");
        btnClearFilter.setBounds(1090, 30, 100, 30);
        btnClearFilter.setBackground(Color.GRAY);
        btnClearFilter.setForeground(Color.WHITE);
        btnClearFilter.addActionListener(e -> clearFilters());
        filterPanel.add(btnClearFilter);

        // ===== ACTION BUTTONS SECTION =====
        JPanel actionPanel = new JPanel(null);
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBounds(50, 180, 1300, 60);
        add(actionPanel);

        JButton btnAdd = new JButton("+ Add Job Order");
        btnAdd.setBounds(20, 10, 180, 40);
        btnAdd.setBackground(AppColors.PRIMARY_RED);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAdd.addActionListener(e -> openAddDialog());
        actionPanel.add(btnAdd);

        JButton btnView = new JButton("View Details");
        btnView.setBounds(220, 10, 150, 40);
        btnView.setBackground(new Color(52, 152, 219));
        btnView.setForeground(Color.WHITE);
        btnView.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnView.addActionListener(e -> openViewDialog());
        actionPanel.add(btnView);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(530, 10, 160, 40);
        btnUpdate.setBackground(new Color(46, 204, 113));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnUpdate.addActionListener(e -> openUpdateDialog());
        actionPanel.add(btnUpdate);


        JButton btnRefresh = new JButton("↻ Refresh");
        btnRefresh.setBounds(390, 10, 120, 40);
        btnRefresh.setBackground(new Color(149, 165, 166));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> loadJobOrders());
        actionPanel.add(btnRefresh);

        JButton btnExport = new JButton("Export to Excel");
        btnExport.setBounds(1130, 10, 150, 40);
        btnExport.setBackground(new Color(39, 174, 96));
        btnExport.setForeground(Color.WHITE);
        btnExport.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnExport.addActionListener(e -> exportToExcel());
        actionPanel.add(btnExport);

        // ===== TABLE SECTION =====
        createJobOrderTable();

        JScrollPane scrollPane = new JScrollPane(jobOrderTable);
        scrollPane.setBounds(50, 260, 1300, 420);
        add(scrollPane);

        // Load initial data
        loadJobOrders();
    }

    private void createJobOrderTable() {
        // Column names matching database structure
        String[] columns = {
                "☑",                    // 0 - Checkbox
                "Job ID",               // 1
                "Client Name",          // 2
                "Department",           // 3
                "Employee Assigned",    // 4
                "Deadline",             // 5
                "Stage",                // 6
                "Status",               // 7
                "Materials",            // 8
                "Job Description",      // 9
                "Customization",        // 10
                "Notes",                // 11
                "Reference No.",        // 12
                "Attachment",           // 13
                "Date Created"          // 14
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Only checkbox is editable
            }
        };

        jobOrderTable = new JTable(tableModel);
        jobOrderTable.setRowHeight(30);
        jobOrderTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        jobOrderTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        jobOrderTable.getTableHeader().setBackground(new Color(52, 73, 94));
        jobOrderTable.getTableHeader().setForeground(Color.WHITE);
        jobOrderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        jobOrderTable.getColumnModel().getColumn(0).setPreferredWidth(40);   // Checkbox
        jobOrderTable.getColumnModel().getColumn(1).setPreferredWidth(100);  // Job ID
        jobOrderTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Client Name
        jobOrderTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Department
        jobOrderTable.getColumnModel().getColumn(4).setPreferredWidth(130);  // Employee
        jobOrderTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Deadline
        jobOrderTable.getColumnModel().getColumn(6).setPreferredWidth(100);  // Stage
        jobOrderTable.getColumnModel().getColumn(7).setPreferredWidth(100);  // Status
        jobOrderTable.getColumnModel().getColumn(8).setPreferredWidth(120);  // Materials
        jobOrderTable.getColumnModel().getColumn(9).setPreferredWidth(200);  // Job Description
        jobOrderTable.getColumnModel().getColumn(10).setPreferredWidth(150); // Customization
        jobOrderTable.getColumnModel().getColumn(11).setPreferredWidth(150); // Notes
        jobOrderTable.getColumnModel().getColumn(12).setPreferredWidth(100); // Reference
        jobOrderTable.getColumnModel().getColumn(13).setPreferredWidth(150); // Attachment
        jobOrderTable.getColumnModel().getColumn(14).setPreferredWidth(100); // Date Created

        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        jobOrderTable.setRowSorter(sorter);

        // Double-click to view details
        jobOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openViewDialog();
                }
            }
        });
    }

    private void loadJobOrders() {
        try {
            List<Object[]> rows = JobOrderDao.getAllJobOrders();
            tableModel.setRowCount(0);

            for (Object[] row : rows) {
                tableModel.addRow(row);
            }

            System.out.println("Loaded " + rows.size() + " job orders");

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
                // Search filter (searches across multiple columns)
                if (!searchText.isEmpty()) {
                    boolean matchFound = false;
                    for (int i = 1; i < entry.getValueCount(); i++) {
                        String value = entry.getStringValue(i).toLowerCase();
                        if (value.contains(searchText)) {
                            matchFound = true;
                            break;
                        }
                    }
                    if (!matchFound) return false;
                }

                // Stage filter
                if (!selectedStage.equals("All")) {
                    String stage = entry.getStringValue(6);
                    if (!stage.equals(selectedStage)) return false;
                }

                // Status filter
                if (!selectedStatus.equals("All")) {
                    String status = entry.getStringValue(7);
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

    private void openAddDialog() {
        AddJobOrderDialog dialog = new AddJobOrderDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                jobOrderTable
        );
        dialog.setVisible(true);
    }

    private void openViewDialog() {
        int selectedRow = jobOrderTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a job order to view.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convert view row to model row (important when filters are applied)
        int modelRow = jobOrderTable.convertRowIndexToModel(selectedRow);

        JobOrderDetailsDialog dialog = new JobOrderDetailsDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                jobOrderTable,
                modelRow
        );
        dialog.setVisible(true);
    }

    private void openUpdateDialog() {
        int selectedRow = jobOrderTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a job order to update.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convert view row to model row
        int modelRow = jobOrderTable.convertRowIndexToModel(selectedRow);

        UpdateJobOrderDialog dialog = new UpdateJobOrderDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                jobOrderTable,
                modelRow
        );
        dialog.setVisible(true);
    }

    private void openDeleteDialog() {
        DeleteJobOrderDialog dialog = new DeleteJobOrderDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                jobOrderTable
        );
        dialog.setVisible(true);
    }

    private void exportToExcel() {
        // Placeholder for Excel export functionality
        JOptionPane.showMessageDialog(this,
                "Excel export feature coming soon!",
                "Export",
                JOptionPane.INFORMATION_MESSAGE);
    }
}