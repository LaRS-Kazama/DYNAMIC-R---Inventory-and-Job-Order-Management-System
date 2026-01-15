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
    private JTextArea txtJobDescription, txtCustomization, txtProgressNotes;
    private JLabel lblAttachment;
    private JTable table;
    private int rowIndex;
    private java.io.File selectedFile;

    public UpdateJobOrderDialog(JFrame parent, JTable orderTable, int row) {
        super(parent, "Update Job Order", true);

        this.table = orderTable;
        this.rowIndex = row;

        setSize(710, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Create scrollable content panel
        JPanel contentPanel = new JPanel(null);
        contentPanel.setPreferredSize(new Dimension(680, 1050));
        contentPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("UPDATE JOB ORDER", SwingConstants.CENTER);
        title.setBounds(0, 10, 680, 40);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        contentPanel.add(title);

        int y = 70;

        // ------- BASIC JOB INFO -------
        JPanel basicPanel = createSectionPanel("Basic Job Order Information", 20, y, 640, 240);
        contentPanel.add(basicPanel);

        int bx = 20, by = 40;
        txtClientName = addField(basicPanel, "Client / Project:", bx, by, getTableValue(row, 2));
        by += 40;

        basicPanel.add(new JLabel("Department:")).setBounds(bx, by, 150, 30);
        cmbDepartment = new JComboBox<>(new String[]{"Mechanical", "Electrical", "Painting", "Inspection"});
        cmbDepartment.setSelectedItem(getTableValue(row, 3));
        cmbDepartment.setBounds(180, by, 420, 30);
        basicPanel.add(cmbDepartment);
        by += 40;

        txtEmployee = addField(basicPanel, "Assigned Employee:", bx, by, getTableValue(row, 4));
        by += 40;

        // DEADLINE - JSpinner with Date
        basicPanel.add(new JLabel("Deadline:")).setBounds(bx, by, 150, 30);
        SpinnerDateModel dateModel = new SpinnerDateModel();
        spinnerDeadline = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDeadline, "yyyy-MM-dd");
        spinnerDeadline.setEditor(dateEditor);
        spinnerDeadline.setBounds(180, by, 420, 30);

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

        y += 260;

        // ------- JOB DESCRIPTION -------
        JPanel scopePanel = createSectionPanel("Job Scope & Customization", 20, y, 640, 240);
        contentPanel.add(scopePanel);

        int sy = 40;
        txtJobDescription = addArea(scopePanel, "Job Description:", sy, getTableValue(row, 9));
        sy += 70;
        txtCustomization = addArea(scopePanel, "Customization:", sy, getTableValue(row, 10));
        sy += 70;

        // MATERIALS DROPDOWN
        JLabel lbl = new JLabel("Materials Required:");
        lbl.setBounds(20, sy, 150, 30);
        scopePanel.add(lbl);

        cmbMaterials = new JComboBox<>();
        cmbMaterials.setBounds(180, sy, 420, 30);
        scopePanel.add(cmbMaterials);

        loadMaterialsFromInventory();
        cmbMaterials.setSelectedItem(getTableValue(row, 8));

        y += 260;

        // ------- PROCESS PANEL -------
        JPanel processPanel = createSectionPanel("Process & Status", 20, y, 640, 220);
        contentPanel.add(processPanel);

        int py = 40;
        processPanel.add(new JLabel("Current Stage:")).setBounds(20, py, 150, 30);

        cmbStage = new JComboBox<>(new String[]{
                "Receiving", "Dismantling", "Rebuilding", "Painting", "Inspection", "Completed"
        });
        cmbStage.setSelectedItem(getTableValue(row, 6));
        cmbStage.setBounds(180, py, 420, 30);
        processPanel.add(cmbStage);

        py += 40;

        // STATUS DROPDOWN
        processPanel.add(new JLabel("Status:")).setBounds(20, py, 150, 30);
        cmbStatus = new JComboBox<>(new String[]{
                "In Progress", "Pending", "On Hold", "Completed"
        });
        cmbStatus.setSelectedItem(getTableValue(row, 7));
        cmbStatus.setBounds(180, py, 420, 30);
        processPanel.add(cmbStatus);

        py += 40;
        txtProgressNotes = addArea(processPanel, "Progress Notes:", py, getTableValue(row, 11));

        y += 240;

        // ------- ATTACHMENTS -------
        JPanel attachPanel = createSectionPanel("Attachments", 20, y, 640, 120);
        contentPanel.add(attachPanel);

        txtReference = addField(attachPanel, "Reference No.:", 20, 40, getTableValue(row, 12));

        JButton btnAttach = new JButton("Attach File");
        btnAttach.setBounds(480, 40, 120, 30);
        attachPanel.add(btnAttach);

        lblAttachment = new JLabel(getTableValue(row, 13).isEmpty() ?
                "No file attached" : "Current: " + getFileName(getTableValue(row, 13)));
        lblAttachment.setBounds(180, 75, 350, 30);
        lblAttachment.setFont(new Font("SansSerif", Font.PLAIN, 11));
        attachPanel.add(lblAttachment);

        btnAttach.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fc.getSelectedFile();
                lblAttachment.setText("Attached: " + selectedFile.getName());
            }
        });

        y += 140;

        // ------- SAVE BUTTON -------
        JButton btnSave = new JButton("Save Changes");
        btnSave.setBounds(180, y, 180, 45);
        btnSave.setBackground(AppColors.PRIMARY_RED);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnSave.addActionListener(e -> saveJobOrder());
        contentPanel.add(btnSave);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(380, y, 150, 45);
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnCancel.addActionListener(e -> dispose());
        contentPanel.add(btnCancel);

        // Add scroll pane to the dialog
        JScrollPane scrollPanel = new JScrollPane(contentPanel);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPanel, BorderLayout.CENTER);
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
            table.setValueAt(txtProgressNotes.getText().trim(), rowIndex, 11);
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
            cmbMaterials.addItem("-- Select Material --");
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

    // ---- Helper UI Methods ----
    private JTextField addField(JPanel p, String label, int x, int y, String value) {
        JLabel l = new JLabel(label);
        l.setBounds(x, y, 150, 30);
        p.add(l);

        JTextField f = new JTextField(value);
        f.setBounds(180, y, 420, 30);
        p.add(f);
        return f;
    }

    private JTextArea addArea(JPanel p, String label, int y, String value) {
        JLabel l = new JLabel(label);
        l.setBounds(20, y, 150, 30);
        p.add(l);

        JTextArea a = new JTextArea(value);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);

        JScrollPane sp = new JScrollPane(a);
        sp.setBounds(180, y, 420, 60);
        p.add(sp);
        return a;
    }

    private JPanel createSectionPanel(String title, int x, int y, int w, int h) {
        JPanel p = new JPanel(null);
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.setBounds(x, y, w, h);
        p.setBackground(Color.WHITE);
        return p;
    }
}