package AdminPage;

import CommonConstant.AppColors;
import DaoClass.InventoryDao;
import DaoClass.JobOrderDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddJobOrderDialog extends JDialog {

    private JTextField txtClientName, txtEmployee, txtReference;
    private JSpinner spinnerDeadline;
    private JComboBox<String> cmbDepartment, cmbStage, cmbStatus, cmbMaterials;
    private JTextArea txtJobDescription, txtCustomization, txtProgressNotes;
    private JLabel lblAttachment;

    private JTable jobOrderTable;
    private File selectedFile;

    public AddJobOrderDialog(JFrame parent, JTable jobOrderTable) {
        super(parent, "Add New Job Order", true);

        this.jobOrderTable = jobOrderTable;

        setSize(710, 750);  // FIXED: Adjusted size
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());  // FIXED: Use BorderLayout

        // FIXED: Create scrollable content panel
        JPanel contentPanel = new JPanel(null);
        contentPanel.setPreferredSize(new Dimension(680, 1050));  // FIXED: Set preferred size for scrolling
        contentPanel.setBackground(Color.WHITE);

        JPanel headPanel = new JPanel(null);
        headPanel.setBackground(new Color(22, 160, 133));
        headPanel.setBounds(0, 0, 680, 80);
        contentPanel.add(headPanel);

        JLabel title = new JLabel("ADD NEW JOB ORDER", SwingConstants.CENTER);
        title.setBounds(0, 10, 680, 40);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.white);
        headPanel.add(title);

        int y = 100;

        // ------- BASIC JOB INFO -------
        JPanel basicPanel = createSectionPanel("Basic Job Order Information", 20, y, 640, 240);
        contentPanel.add(basicPanel);

        int bx = 20, by = 40;
        txtClientName = addField(basicPanel, "Client / Project:", bx, by);
        by += 40;

        basicPanel.add(new JLabel("Department:")).setBounds(bx, by, 150, 30);
        cmbDepartment = new JComboBox<>(new String[]{"Mechanical", "Electrical", "Painting", "Inspection"});
        cmbDepartment.setBounds(180, by, 420, 30);
        basicPanel.add(cmbDepartment);
        by += 40;

        txtEmployee = addField(basicPanel, "Assigned Employee:", bx, by);
        by += 40;

        // DEADLINE - JSpinner with Date
        basicPanel.add(new JLabel("Deadline:")).setBounds(bx, by, 150, 30);
        SpinnerDateModel dateModel = new SpinnerDateModel();
        spinnerDeadline = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDeadline, "yyyy-MM-dd");
        spinnerDeadline.setEditor(dateEditor);
        spinnerDeadline.setBounds(180, by, 420, 30);
        basicPanel.add(spinnerDeadline);

        y += 260;

        // ------- JOB DESCRIPTION -------
        JPanel scopePanel = createSectionPanel("Job Scope & Customization", 20, y, 640, 240);
        contentPanel.add(scopePanel);

        int sy = 40;
        txtJobDescription = addArea(scopePanel, "Job Description:", sy);
        sy += 70;
        txtCustomization = addArea(scopePanel, "Customization:", sy);
        sy += 70;

        // MATERIALS DROPDOWN
        JLabel lbl = new JLabel("Materials Required:");
        lbl.setBounds(20, sy, 150, 30);
        scopePanel.add(lbl);

        cmbMaterials = new JComboBox<>();
        cmbMaterials.setBounds(180, sy, 420, 30);
        scopePanel.add(cmbMaterials);

        loadMaterialsFromInventory();

        y += 260;

        // ------- PROCESS PANEL -------
        JPanel processPanel = createSectionPanel("Process & Status", 20, y, 640, 220);
        contentPanel.add(processPanel);

        int py = 40;
        processPanel.add(new JLabel("Current Stage:")).setBounds(20, py, 150, 30);

        cmbStage = new JComboBox<>(new String[]{
                "Receiving", "Dismantling", "Rebuilding", "Painting", "Inspection", "Completed"
        });
        cmbStage.setBounds(180, py, 420, 30);
        processPanel.add(cmbStage);

        py += 40;

        // STATUS DROPDOWN
        processPanel.add(new JLabel("Status:")).setBounds(20, py, 150, 30);
        cmbStatus = new JComboBox<>(new String[]{
                "In Progress", "Pending", "On Hold", "Completed"
        });
        cmbStatus.setBounds(180, py, 420, 30);
        processPanel.add(cmbStatus);

        py += 40;
        txtProgressNotes = addArea(processPanel, "Progress Notes:", py);

        y += 240;

        // ------- ATTACHMENTS -------
        JPanel attachPanel = createSectionPanel("Attachments", 20, y, 640, 120);
        contentPanel.add(attachPanel);

        txtReference = addField(attachPanel, "Reference No.:", 20, 40);

        JButton btnAttach = new JButton("Attach File");
        btnAttach.setBounds(480, 40, 120, 30);
        attachPanel.add(btnAttach);

        lblAttachment = new JLabel("No file selected");
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
        JButton btnSave = new JButton("Save Job Order");
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

        // FIXED: Add scroll pane to the dialog with faster scrolling
        JScrollPane scrollPanel = new JScrollPane(contentPanel);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(20);  // INCREASED: Faster scrolling with mouse wheel
        scrollPanel.getVerticalScrollBar().setBlockIncrement(100); // ADDED: Faster page up/down scrolling
        add(scrollPanel, BorderLayout.CENTER);
    }

    // ---- SAVE JOB ORDER ----
    private void saveJobOrder() {
        try {
            // Validate required fields
            if (txtClientName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter client name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (txtEmployee.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter assigned employee.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Convert date
            Date utilDate = (Date) spinnerDeadline.getValue();
            java.sql.Date sqlDeadline = new java.sql.Date(utilDate.getTime());

            System.out.println("Saving job order...");
            System.out.println("Client: " + txtClientName.getText());
            System.out.println("Deadline: " + sqlDeadline);

            boolean success = JobOrderDao.addJobOrder(
                    txtClientName.getText().trim(),
                    cmbDepartment.getSelectedItem().toString(),
                    txtEmployee.getText().trim(),
                    sqlDeadline,
                    cmbStage.getSelectedItem().toString(),
                    cmbStatus.getSelectedItem().toString(),
                    cmbMaterials.getSelectedItem() != null ? cmbMaterials.getSelectedItem().toString() : null,
                    txtJobDescription.getText().trim(),
                    txtCustomization.getText().trim(),
                    txtProgressNotes.getText().trim(),
                    txtReference.getText().trim(),
                    selectedFile == null ? null : selectedFile.getAbsolutePath()
            );

            if (!success) {
                JOptionPane.showMessageDialog(this, "Failed to save job order.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("Job order saved successfully!");

            // ---- REFRESH JOB ORDER TABLE ----
            refreshJobOrderTable();

            JOptionPane.showMessageDialog(this, "Job Order added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            System.err.println("Error in saveJobOrder:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving job order: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshJobOrderTable() {
        try {
            System.out.println("Refreshing table...");

            if (jobOrderTable == null) {
                System.err.println("ERROR: jobOrderTable is null!");
                return;
            }

            List<Object[]> rows = JobOrderDao.getAllJobOrders();
            System.out.println("Retrieved " + rows.size() + " rows from database");

            DefaultTableModel model = (DefaultTableModel) jobOrderTable.getModel();
            model.setRowCount(0);

            for (Object[] row : rows) {
                model.addRow(row);
            }

            System.out.println("Table refreshed with " + model.getRowCount() + " rows");

        } catch (Exception e) {
            System.err.println("Error refreshing table:");
            e.printStackTrace();
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

    // ---- Helper UI Methods ----
    private JTextField addField(JPanel p, String label, int x, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(x, y, 150, 30);
        p.add(l);

        JTextField f = new JTextField();
        f.setBounds(180, y, 420, 30);
        p.add(f);
        return f;
    }

    private JTextArea addArea(JPanel p, String label, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(20, y, 150, 30);
        p.add(l);

        JTextArea a = new JTextArea();
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