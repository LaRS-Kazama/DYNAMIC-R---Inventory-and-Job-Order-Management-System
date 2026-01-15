package AdminPage;

import CommonConstant.AppColors;
import DaoClass.InventoryDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AddItemDialog extends JDialog {
    private JTextField txtItemName, txtBatchNo, txtLocation;
    private JSpinner spinnerQuantity;
    private JComboBox<String> cmbCategory, cmbStatus;
    private DefaultTableModel tableModel;

    public AddItemDialog(JFrame parent, DefaultTableModel model) {
        super(parent, "Add New Inventory Item", true);
        this.tableModel = model;

        setSize(550, 550);
        setLocationRelativeTo(parent);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBounds(0, 0, 550, 70);
        add(headerPanel);

        JLabel lblTitle = new JLabel("ADD NEW INVENTORY ITEM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 20, 550, 30);
        headerPanel.add(lblTitle);

        int labelX = 50, fieldX = 200, width = 300, height = 30;
        int y = 100;

        // Item Name
        addLabel("Item Name:", labelX, y);
        txtItemName = createTextField(fieldX, y, width, height);
        add(txtItemName);
        y += 50;

        // Category
        addLabel("Category:", labelX, y);
        cmbCategory = new JComboBox<>(new String[]{
                "Engine", "Transmission", "Suspension", "Electrical", "Body Parts",
                "Tools", "Materials", "Equipment", "Parts", "Consumables"
        });
        cmbCategory.setBounds(fieldX, y, width, height);
        cmbCategory.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbCategory.setBackground(Color.WHITE);
        add(cmbCategory);
        y += 50;

        // Quantity with Spinner
        addLabel("Quantity:", labelX, y);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 0, 100000, 1);
        spinnerQuantity = new JSpinner(spinnerModel);
        spinnerQuantity.setBounds(fieldX, y, 150, height);
        spinnerQuantity.setFont(new Font("SansSerif", Font.PLAIN, 14));
        add(spinnerQuantity);
        y += 50;

        // Batch No
        addLabel("Batch No.:", labelX, y);
        txtBatchNo = createTextField(fieldX, y, width, height);
        add(txtBatchNo);
        y += 50;

        // Location
        addLabel("Location:", labelX, y);
        txtLocation = createTextField(fieldX, y, width, height);
        txtLocation.setText("Warehouse A"); // Default value
        add(txtLocation);
        y += 50;

        // Status
        addLabel("Status:", labelX, y);
        cmbStatus = new JComboBox<>(new String[]{
                "Available", "Low Stock", "Out of Stock", "Damaged", "Scrapped", "Returned"
        });
        cmbStatus.setBounds(fieldX, y, width, height);
        cmbStatus.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbStatus.setBackground(Color.WHITE);
        add(cmbStatus);
        y += 50;

        // Info label about auto date
        JLabel lblInfo = new JLabel("* Date will be added automatically");
        lblInfo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblInfo.setForeground(new Color(100, 100, 100));
        lblInfo.setBounds(50, y, 450, 25);
        add(lblInfo);
        y += 40;

        // ===== Buttons =====
        JButton btnSave = new JButton("Save Item");
        btnSave.setBounds(140, 470, 130, 40);
        btnSave.setBackground(AppColors.PRIMARY_RED);
        btnSave.setForeground(AppColors.LIGHT_TEXT);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSave.setFocusPainted(false);
        btnSave.setBorder(BorderFactory.createEmptyBorder());
        add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(290, 470, 130, 40);
        btnCancel.setBackground(new Color(149, 165, 166));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(BorderFactory.createEmptyBorder());
        add(btnCancel);

        // Hover effects
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnSave.setBackground(AppColors.PRIMARY_RED_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnSave.setBackground(AppColors.PRIMARY_RED);
            }
        });

        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCancel.setBackground(new Color(127, 140, 141));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnCancel.setBackground(new Color(149, 165, 166));
            }
        });

        // Button Actions
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> saveItem());
    }

    private void saveItem() {
        try {
            String itemName = txtItemName.getText().trim();
            String batchNo = txtBatchNo.getText().trim();
            String location = txtLocation.getText().trim();
            String category = cmbCategory.getSelectedItem().toString();
            String status = cmbStatus.getSelectedItem().toString();
            int quantity = (Integer) spinnerQuantity.getValue();

            // Input validation
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

            // Save to database (ID and date are auto-generated)
            boolean success = InventoryDao.addItem(itemName, category, quantity,
                    batchNo, location, status);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Item successfully added!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to add item.",
                        "Database Error",
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

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setBounds(x, y, 140, 30);
        lbl.setForeground(AppColors.DARK_TEXT);
        add(lbl);
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField field = new JTextField();
        field.setBounds(x, y, width, height);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
}