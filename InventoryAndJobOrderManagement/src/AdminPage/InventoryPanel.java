package AdminPage;

import CommonConstant.AppColors;
import DaoClass.InventoryDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbFilterCategory, cmbFilterStatus;

    // Store panels as instance variables for refresh
    private JPanel filterPanel;
    private JPanel actionPanel;
    private JScrollPane tableScrollPane;

    public InventoryPanel() {
        setLayout(null);
        setBackground(new Color(245, 245, 245));

        // ===== HEADER SECTION =====
        JLabel title = new JLabel("INVENTORY MANAGEMENT");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(50, 50, 50));
        title.setBounds(50, 25, 500, 40);
        add(title);

        // ===== QUICK STATS CARDS =====
        createQuickStatsCards();

        // ===== SEARCH AND FILTER SECTION =====
        createFilterPanel();

        // ===== ACTION BUTTONS SECTION =====
        createActionPanel();

        // ===== TABLE SECTION =====
        createInventoryTable();

        tableScrollPane = new JScrollPane(inventoryTable);
        tableScrollPane.setBounds(50, 360, 1300, 320);
        add(tableScrollPane);

        // Load initial data
        loadInventoryData();
    }

    private void createFilterPanel() {
        filterPanel = new JPanel(null);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));
        filterPanel.setBounds(50, 180, 1300, 80);
        add(filterPanel);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setBounds(20, 30, 80, 30);
        filterPanel.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(100, 30, 250, 30);
        filterPanel.add(txtSearch);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setBounds(380, 30, 80, 30);
        filterPanel.add(lblCategory);

        cmbFilterCategory = new JComboBox<>(new String[]{
                "All", "Tools", "Materials", "Equipment", "Parts", "Consumables"
        });
        cmbFilterCategory.setBounds(470, 30, 180, 30);
        filterPanel.add(cmbFilterCategory);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(680, 30, 80, 30);
        filterPanel.add(lblStatus);

        cmbFilterStatus = new JComboBox<>(new String[]{
                "All", "Available", "Low Stock", "Out of Stock", "Damaged"
        });
        cmbFilterStatus.setBounds(750, 30, 180, 30);
        filterPanel.add(cmbFilterStatus);

        JButton btnApplyFilter = new JButton("Apply Filter");
        btnApplyFilter.setBounds(960, 30, 130, 30);
        btnApplyFilter.setBackground(AppColors.PRIMARY_RED);
        btnApplyFilter.setForeground(Color.WHITE);
        btnApplyFilter.addActionListener(e -> applyFilters());
        filterPanel.add(btnApplyFilter);

        JButton btnClearFilter = new JButton("Clear");
        btnClearFilter.setBounds(1110, 30, 100, 30);
        btnClearFilter.setBackground(Color.GRAY);
        btnClearFilter.setForeground(Color.WHITE);
        btnClearFilter.addActionListener(e -> clearFilters());
        filterPanel.add(btnClearFilter);
    }

    private void createActionPanel() {
        actionPanel = new JPanel(null);
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBounds(50, 280, 1300, 60);
        add(actionPanel);

        JButton btnAdd = new JButton("+ Add Item");
        btnAdd.setBounds(20, 10, 150, 40);
        btnAdd.setBackground(AppColors.PRIMARY_RED);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAdd.addActionListener(e -> openAddDialog());
        actionPanel.add(btnAdd);

        JButton btnUpdate = new JButton("Update Item");
        btnUpdate.setBounds(190, 10, 150, 40);
        btnUpdate.setBackground(new Color(46, 204, 113));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnUpdate.addActionListener(e -> openUpdateDialog());
        actionPanel.add(btnUpdate);

        JButton btnRefresh = new JButton("↻ Refresh");
        btnRefresh.setBounds(360, 10, 120, 40);
        btnRefresh.setBackground(new Color(149, 165, 166));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> refreshInventory());
        actionPanel.add(btnRefresh);

        JButton btnExport = new JButton("Export to Excel");
        btnExport.setBounds(1130, 10, 150, 40);
        btnExport.setBackground(new Color(39, 174, 96));
        btnExport.setForeground(Color.WHITE);
        btnExport.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnExport.addActionListener(e -> exportToExcel());
        actionPanel.add(btnExport);
    }

    private void createQuickStatsCards() {
        // Remove old stat cards if they exist
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                // Remove panels that are stat cards (positioned at y=80)
                if (panel.getBounds().y == 80) {
                    remove(panel);
                }
            }
        }

        int totalItems = InventoryDao.getTotalItems();
        int lowStock = InventoryDao.getLowStockCount();
        int damaged = InventoryDao.getDamagedCount();
        int available = totalItems - lowStock - damaged;

        JPanel cardTotal = createStatCard("Total Items", totalItems,
                new Color(52, 152, 219), "📦");
        cardTotal.setBounds(50, 80, 300, 80);
        add(cardTotal);

        JPanel cardLowStock = createStatCard("Low Stock", lowStock,
                new Color(230, 126, 34), "⚠");
        cardLowStock.setBounds(380, 80, 300, 80);
        add(cardLowStock);

        JPanel cardDamaged = createStatCard("Damaged Items", damaged,
                new Color(231, 76, 60), "⚡");
        cardDamaged.setBounds(710, 80, 300, 80);
        add(cardDamaged);

        JPanel cardAvailable = createStatCard("Available", available,
                new Color(46, 204, 113), "✓");
        cardAvailable.setBounds(1040, 80, 300, 80);
        add(cardAvailable);
    }

    private JPanel createStatCard(String title, int count, Color color, String icon) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 30));
        lblIcon.setBounds(15, 15, 50, 50);
        card.add(lblIcon);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTitle.setForeground(Color.DARK_GRAY);
        lblTitle.setBounds(75, 15, 200, 20);
        card.add(lblTitle);

        JLabel lblCount = new JLabel(String.valueOf(count));
        lblCount.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblCount.setForeground(color);
        lblCount.setBounds(75, 35, 200, 30);
        card.add(lblCount);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    private void createInventoryTable() {
        String[] columns = {
                "☑", "Item ID", "Item Name", "Category", "Quantity",
                "Unit", "Status", "Batch No.", "Location",  "Date Added"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        inventoryTable = new JTable(tableModel);
        inventoryTable.setRowHeight(30);
        inventoryTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        inventoryTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        inventoryTable.getTableHeader().setBackground(new Color(52, 73, 94));
        inventoryTable.getTableHeader().setForeground(Color.WHITE);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        inventoryTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        inventoryTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        inventoryTable.getColumnModel().getColumn(5).setPreferredWidth(70);
        inventoryTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        inventoryTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        inventoryTable.getColumnModel().getColumn(8).setPreferredWidth(120);
        inventoryTable.getColumnModel().getColumn(9).setPreferredWidth(100);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        inventoryTable.setRowSorter(sorter);
    }

    private void loadInventoryData() {
        try {
            List<Object[]> rows = InventoryDao.getAllItems();
            tableModel.setRowCount(0);

            for (Object[] row : rows) {
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading inventory: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshInventory() {
        // Only refresh the stats cards and table data
        createQuickStatsCards();
        loadInventoryData();

        revalidate();
        repaint();

        JOptionPane.showMessageDialog(this,
                "Inventory refreshed successfully!",
                "Refresh Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void applyFilters() {
        String searchText = txtSearch.getText().toLowerCase().trim();
        String selectedCategory = cmbFilterCategory.getSelectedItem().toString();
        String selectedStatus = cmbFilterStatus.getSelectedItem().toString();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        inventoryTable.setRowSorter(sorter);

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
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

                if (!selectedCategory.equals("All")) {
                    String category = entry.getStringValue(3);
                    if (!category.equals(selectedCategory)) return false;
                }

                if (!selectedStatus.equals("All")) {
                    String status = entry.getStringValue(6);
                    if (!status.equals(selectedStatus)) return false;
                }

                return true;
            }
        });
    }

    private void clearFilters() {
        txtSearch.setText("");
        cmbFilterCategory.setSelectedIndex(0);
        cmbFilterStatus.setSelectedIndex(0);
        inventoryTable.setRowSorter(new TableRowSorter<>(tableModel));
    }

    private void openAddDialog() {
        AddItemDialog dialog = new AddItemDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                tableModel
        );
        dialog.setVisible(true);
        refreshInventory();
    }

    private void openUpdateDialog() {
        int selectedRow = -1;
        for (int i = 0; i < inventoryTable.getRowCount(); i++) {
            Boolean isChecked = (Boolean) inventoryTable.getValueAt(i, 0);
            if (isChecked != null && isChecked) {
                selectedRow = i;
                break;
            }
        }

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an item to update.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        UpdateItemDialog dialog = new UpdateItemDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                inventoryTable,
                selectedRow
        );
        dialog.setVisible(true);
        refreshInventory();
    }

    private void exportToExcel() {
        JOptionPane.showMessageDialog(this,
                "Excel export feature coming soon!",
                "Export",
                JOptionPane.INFORMATION_MESSAGE);
    }
}