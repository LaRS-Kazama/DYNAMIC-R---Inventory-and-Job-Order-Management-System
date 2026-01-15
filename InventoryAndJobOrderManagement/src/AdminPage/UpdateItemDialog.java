package AdminPage;

import CommonConstant.AppColors;
import DaoClass.InventoryDao;

import javax.swing.*;
import java.awt.*;

public class UpdateItemDialog extends JDialog {
    private JTextField txtItemName, txtBatchNo, txtLocation;
    private JSpinner spinnerQuantity;
    private JComboBox<String> cmbCategory, cmbStatus;
    private JTable table;
    private final int rowIndex;

    public UpdateItemDialog(JFrame parent, JTable table, int rowIndex) {
        super(parent, "Update Inventory Item", true);
        this.table = table;
        this.rowIndex = rowIndex;

        setSize(550, 580);
        setLocationRelativeTo(parent);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBounds(0, 0, 550, 70);
        add(headerPanel);

        JLabel lblTitle = new JLabel("UPDATE INVENTORY ITEM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 15, 550, 30);
        headerPanel.add(lblTitle);

        String itemId = getTableValue(rowIndex, 1);
        JLabel lblItemId = new JLabel("ID: " + itemId);
        lblItemId.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblItemId.setForeground(Color.WHITE);
        lblItemId.setBounds(200, 45, 150, 20);
        headerPanel.add(lblItemId);

        int labelX = 50, fieldX = 200, width = 300, height = 30;
        int y = 100;

        // Item Name
        addLabel("Item Name:", labelX, y);
        txtItemName = createField(getTableValue(rowIndex, 2), fieldX, y, width, height);
        add(txtItemName);
        y += 50;

        // Category
        addLabel("Category:", labelX, y);
        cmbCategory = new JComboBox<>(new String[]{
                "Engine", "Transmission", "Suspension", "Electrical", "Body Parts",
                "Tools", "Materials", "Equipment", "Parts", "Consumables"
        });
        cmbCategory.setBounds(fieldX, y, width, height);
        cmbCategory.setSelectedItem(getTableValue(rowIndex, 3));
        cmbCategory.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(cmbCategory);
        y += 50;

        // Quantity with Spinner
        addLabel("Quantity:", labelX, y);
        int currentQty = 0;
        try {
            currentQty = Integer.parseInt(getTableValue(rowIndex, 4));
        } catch (NumberFormatException e) {
            currentQty = 0;
        }
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(currentQty, 0, 100000, 1);
        spinnerQuantity = new JSpinner(spinnerModel);
        spinnerQuantity.setBounds(fieldX, y, 150, height);
        spinnerQuantity.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(spinnerQuantity);
        y += 50;

        // Status
        addLabel("Status:", labelX, y);
        cmbStatus = new JComboBox<>(new String[]{
                "Available", "Low Stock", "Out of Stock", "Damaged", "Scrapped", "Returned"
        });
        cmbStatus.setBounds(fieldX, y, width, height);
        cmbStatus.setSelectedItem(getTableValue(rowIndex, 6));
        cmbStatus.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(cmbStatus);
        y += 50;

        // Batch No
        addLabel("Batch No.:", labelX, y);
        txtBatchNo = createField(getTableValue(rowIndex, 7), fieldX, y, width, height);
        add(txtBatchNo);
        y += 50;

        // Location
        addLabel("Location:", labelX, y);
        txtLocation = createField(getTableValue(rowIndex, 8), fieldX, y, width, height);
        add(txtLocation);
        y += 50;

        // Date info (read-only)
        String dateAdded = getTableValue(rowIndex, 9);
        JLabel lblDateInfo = new JLabel("Date Added: " + dateAdded + " (cannot be changed)");
        lblDateInfo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblDateInfo.setForeground(new Color(100, 100, 100));
        lblDateInfo.setBounds(50, y, 450, 25);
        add(lblDateInfo);
        y += 40;

        // ===== Buttons =====
        JButton btnSave = new JButton("Save Changes");
        btnSave.setBounds(140, 500, 130, 40);
        btnSave.setBackground(AppColors.PRIMARY_RED);
        btnSave.setForeground(AppColors.LIGHT_TEXT);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSave.setFocusPainted(false);
        add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(290, 500, 130, 40);
        btnCancel.setBackground(new Color(149, 165, 166));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCancel.setFocusPainted(false);
        add(btnCancel);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> saveChanges());
    }

    private void saveChanges() {
        try {
            String itemId = getTableValue(rowIndex, 1);
            String itemName = txtItemName.getText().trim();
            String category = cmbCategory.getSelectedItem().toString();
            int quantity = (Integer) spinnerQuantity.getValue();
            String status = cmbStatus.getSelectedItem().toString();
            String batchNo = txtBatchNo.getText().trim();
            String location = txtLocation.getText().trim();

            // Validation
            if (itemName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter item name.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (batchNo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter batch number.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (location.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter location.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Update in database
            boolean updated = InventoryDao.updateItem(itemId, itemName, category,
                    quantity, batchNo, location, status);

            if (updated) {
                // Update table visually
                table.setValueAt(itemName, rowIndex, 2);
                table.setValueAt(category, rowIndex, 3);
                table.setValueAt(quantity, rowIndex, 4);
                table.setValueAt("pcs", rowIndex, 5); // Unit
                table.setValueAt(status, rowIndex, 6);
                table.setValueAt(batchNo, rowIndex, 7);
                table.setValueAt(location, rowIndex, 8);

                JOptionPane.showMessageDialog(this,
                        "Item updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update item in database.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getTableValue(int row, int col) {
        Object value = table.getValueAt(row, col);
        return value != null ? value.toString() : "";
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setBounds(x, y, 140, 30);
        lbl.setForeground(AppColors.DARK_TEXT);
        add(lbl);
    }

    private JTextField createField(String value, int x, int y, int width, int height) {
        JTextField field = new JTextField(value);
        field.setBounds(x, y, width, height);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
}