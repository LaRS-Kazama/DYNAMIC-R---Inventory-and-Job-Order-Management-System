package AdminPage;

import CommonConstant.AppColors;
import DaoClass.InventoryDao;
import DaoClass.JobOrderDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UpdateJobOrderDialog extends JDialog {

    private JTextField txtClientName, txtEmployee, txtReference;
    private JSpinner spinnerDeadline;
    private JComboBox<String> cmbDepartment, cmbStage, cmbStatus, cmbMaterials;
    private JTextArea txtJobDescription, txtCustomization, txtNotes;
    private JLabel lblAttachment;
    private JTable table;
    private int rowIndex;
    private java.io.File selectedFile;

    public UpdateJobOrderDialog(JFrame parent, JTable orderTable, int row) {
        super(parent, "Update Job Order", true);

        this.table = orderTable;
        this.rowIndex = row;

        setSize(600, 850);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("UPDATE JOB ORDER", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBounds(0, 20, 600, 35);
        mainPanel.add(title);

        int y = 80;

        // ------- BASIC INFO SECTION -------
        JPanel basicPanel = createSection("Basic Information", 30, y, 540, 280);
        mainPanel.add(basicPanel);

        int by = 40;
        txtClientName = addField(basicPanel, "Client / Project:", 20, by,
                getTableValue(row, 2));
        by += 45;

        cmbDepartment = addCombo(basicPanel, "Department:", 20, by,
                new String[]{"Mechanical", "Electrical", "Painting", "Inspection"},
                getTableValue(row, 3));
        by += 45;

        txtEmployee = addField(basicPanel, "Assigned Employee:", 20, by,
                getTableValue(row, 4));
        by += 45;

        // Deadline with JSpinner
        JLabel lblDeadline = new JLabel("Deadline:");
        lblDeadline.setBounds(20, by, 150, 25);
        basicPanel.add(lblDeadline);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        spinnerDeadline = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDeadline, "yyyy-MM-dd");
        spinnerDeadline.setEditor(dateEditor);
        spinnerDeadline.setBounds(180, by, 320, 30);

        // Set initial date value
        try {
            Object deadlineObj = table.getValueAt(row, 5);
            if (deadlineObj != null) {
                if (deadlineObj instanceof java.sql.Date) {
                    spinnerDeadline.setValue(new Date(((java.sql.Date) deadlineObj).getTime()));
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(deadlineObj.toString());
                    spinnerDeadline.setValue(date);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        basicPanel.add(spinnerDeadline);
        by += 45;

        cmbStage = addCombo(basicPanel, "Stage:", 20, by,
                new String[]{"Receiving", "Dismantling", "Rebuilding", "Painting", "Inspection", "Completed"},
                getTableValue(row, 6));
        by += 45;

        cmbStatus = addCombo(basicPanel, "Status:", 20, by,
                new String[]{"In Progress", "Pending", "On Hold", "Completed"},
                getTableValue(row, 7));

        y += 300;

        // ------- JOB DETAILS SECTION -------
        JPanel detailsPanel = createSection("Job Details", 30, y, 540, 280);
        mainPanel.add(detailsPanel);

        int dy = 40;

        // Materials dropdown
        JLabel lblMaterials = new JLabel("Materials:");
        lblMaterials.setBounds(20, dy, 150, 25);
        detailsPanel.add(lblMaterials);

        cmbMaterials = new JComboBox<>();
        loadMaterialsFromInventory();
        cmbMaterials.setSelectedItem(getTableValue(row, 8));
        cmbMaterials.setBounds(180, dy, 320, 30);
        detailsPanel.add(cmbMaterials);
        dy += 45;

        txtJobDescription = addArea(detailsPanel, "Job Description:", 20, dy,
                getTableValue(row, 9));
        dy += 70;

        txtCustomization = addArea(detailsPanel, "Customization:", 20, dy,
                getTableValue(row, 10));
        dy += 70;

        txtNotes = addArea(detailsPanel, "Notes:", 20, dy,
                getTableValue(row, 11));

        y += 300;

        // ------- REFERENCE & ATTACHMENT SECTION -------
        JPanel attachPanel = createSection("Reference & Attachments", 30, y, 540, 120);
        mainPanel.add(attachPanel);

        txtReference = addField(attachPanel, "Reference No.:", 20, 40,
                getTableValue(row, 12));

        JButton btnAttach = new JButton("Change File");
        btnAttach.setBounds(380, 40, 120, 30);
        attachPanel.add(btnAttach);

        lblAttachment = new JLabel(getTableValue(row, 13).isEmpty() ?
                "No file attached" : "Current: " + getFileName(getTableValue(row, 13)));
        lblAttachment.setBounds(180, 80, 320, 25);
        lblAttachment.setFont(new Font("SansSerif", Font.PLAIN, 11));
        attachPanel.add(lblAttachment);

        btnAttach.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fc.getSelectedFile();
                lblAttachment.setText("New: " + selectedFile.getName());
            }
        });

        // ------- BUTTONS -------
        JButton btnSave = new JButton("Save Changes");
        btnSave.setBounds(150, y + 160, 150, 40);
        btnSave.setBackground(AppColors.PRIMARY_RED);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSave.addActionListener(e -> saveJobOrder());
        mainPanel.add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(320, y + 160, 150, 40);
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCancel.addActionListener(e -> dispose());
        mainPanel.add(btnCancel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);
    }

    private void saveJobOrder() {
        try {
            String id = table.getValueAt(rowIndex, 1).toString();

            // Validate required fields
            if (txtClientName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter client name.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (txtEmployee.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter assigned employee.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Convert date
            Date utilDate = (Date) spinnerDeadline.getValue();
            java.sql.Date sqlDeadline = new java.sql.Date(utilDate.getTime());

            // FIXED: Use updateJobOrder() instead of addJobOrder()
            boolean ok = JobOrderDao.updateJobOrder(
                    id,
                    txtClientName.getText().trim(),
                    cmbDepartment.getSelectedItem().toString(),
                    txtEmployee.getText().trim(),
                    sqlDeadline,
                    cmbStage.getSelectedItem().toString(),
                    cmbStatus.getSelectedItem().toString()
            );

            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Failed to update job order in database.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update JTable visually
            table.setValueAt(txtClientName.getText().trim(), rowIndex, 2);
            table.setValueAt(cmbDepartment.getSelectedItem(), rowIndex, 3);
            table.setValueAt(txtEmployee.getText().trim(), rowIndex, 4);
            table.setValueAt(sqlDeadline, rowIndex, 5);
            table.setValueAt(cmbStage.getSelectedItem(), rowIndex, 6);
            table.setValueAt(cmbStatus.getSelectedItem(), rowIndex, 7);
            table.setValueAt(cmbMaterials.getSelectedItem(), rowIndex, 8);
            table.setValueAt(txtJobDescription.getText().trim(), rowIndex, 9);
            table.setValueAt(txtCustomization.getText().trim(), rowIndex, 10);
            table.setValueAt(txtNotes.getText().trim(), rowIndex, 11);
            table.setValueAt(txtReference.getText().trim(), rowIndex, 12);

            if (selectedFile != null) {
                table.setValueAt(selectedFile.getAbsolutePath(), rowIndex, 13);
            }

            JOptionPane.showMessageDialog(this,
                    "Job Order updated successfully!",
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

    private void loadMaterialsFromInventory() {
        cmbMaterials.removeAllItems();
        List<String> items = InventoryDao.getAvailableItemNamesForJobOrder();

        if (items.isEmpty()) {
            cmbMaterials.addItem("No materials available");
            cmbMaterials.setEnabled(false);
        } else {
            for (String s : items) {
                cmbMaterials.addItem(s);
            }
        }
    }

    private String getTableValue(int row, int col) {
        Object value = table.getValueAt(row, col);
        return value != null ? value.toString() : "";
    }

    private String getFileName(String path) {
        if (path == null || path.isEmpty()) return "";
        return new java.io.File(path).getName();
    }

    // Helper UI Methods
    private JTextField addField(JPanel panel, String label, int x, int y, String value) {
        JLabel l = new JLabel(label);
        l.setBounds(x, y, 150, 25);
        panel.add(l);

        JTextField f = new JTextField(value);
        f.setBounds(180, y, 320, 30);
        panel.add(f);
        return f;
    }

    private JComboBox<String> addCombo(JPanel panel, String label, int x, int y,
                                       String[] items, String selected) {
        JLabel l = new JLabel(label);
        l.setBounds(x, y, 150, 25);
        panel.add(l);

        JComboBox<String> c = new JComboBox<>(items);
        c.setBounds(180, y, 320, 30);
        c.setSelectedItem(selected);
        panel.add(c);
        return c;
    }

    private JTextArea addArea(JPanel panel, String label, int x, int y, String value) {
        JLabel l = new JLabel(label);
        l.setBounds(x, y, 150, 25);
        panel.add(l);

        JTextArea a = new JTextArea(value);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);

        JScrollPane sp = new JScrollPane(a);
        sp.setBounds(180, y, 320, 60);
        panel.add(sp);
        return a;
    }

    private JPanel createSection(String title, int x, int y, int w, int h) {
        JPanel p = new JPanel(null);
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.setBounds(x, y, w, h);
        p.setBackground(Color.WHITE);
        return p;
    }
}