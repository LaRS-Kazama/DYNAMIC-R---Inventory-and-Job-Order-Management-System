package AdminPage;

import DaoClass.InventoryDao;
import DaoClass.JobOrderDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private DefaultTableModel activityModel;
    private JTable activityTable;
    private JPanel chartPanel;
    private JPanel activityPanel;
    private JLabel dashboardTitle;
    private JButton btnRefresh;

    public DashboardPanel() {
        setLayout(null);
        setBackground(new Color(245, 245, 245));

        // Dashboard Title
        dashboardTitle = new JLabel("ADMIN DASHBOARD");
        dashboardTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        dashboardTitle.setForeground(new Color(50, 50, 50));
        dashboardTitle.setBounds(60, 25, 400, 40);
        add(dashboardTitle);

        // Refresh button
        btnRefresh = new JButton("↻ Refresh Dashboard");
        btnRefresh.setBounds(1150, 25, 180, 40);
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> refreshDashboard());
        add(btnRefresh);

        // ===== Summary Cards =====
        createSummaryCards();

        // ===== Job Order Status Chart =====
        createJobOrderChart();

        // ===== Recent Activity Log =====
        createActivityLog();

        // Initial load of activities
        loadRecentActivities();
    }

    private void createSummaryCards() {
        // Remove old stat cards if they exist
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                // Remove panels that are stat cards (positioned between y=80 and y=200)
                Rectangle bounds = panel.getBounds();
                if (bounds.y >= 80 && bounds.y <= 200 && bounds.height == 100) {
                    remove(panel);
                }
            }
        }

        // Get fresh data
        int totalItems = InventoryDao.getTotalItems();
        int lowStock = InventoryDao.getLowStockCount();
        int damaged = InventoryDao.getDamagedCount();
        int activeJO = JobOrderDao.getActiveJobOrders();
        int completedJO = JobOrderDao.getCompletedJobOrders();

        // Card 1: Total Inventory
        JPanel cardInventory = createSummaryCard(
                "Total Inventory Items",
                totalItems,
                new Color(52, 152, 219),
                "View all items in inventory"
        );
        cardInventory.setBounds(50, 80, 300, 100);
        add(cardInventory);

        // Card 2: Low Stock
        JPanel cardLowStock = createSummaryCard(
                "Low Stock Items",
                lowStock,
                new Color(230, 126, 34),
                lowStock > 0 ? "⚠ Requires attention!" : "All items stocked"
        );
        cardLowStock.setBounds(380, 80, 300, 100);
        add(cardLowStock);

        // Card 3: Damaged Items
        JPanel cardDamaged = createSummaryCard(
                "Damaged / Returned",
                damaged,
                new Color(231, 76, 60),
                damaged > 0 ? "⚠ Needs review" : "No damaged items"
        );
        cardDamaged.setBounds(710, 80, 300, 100);
        add(cardDamaged);

        // Card 4: Active Job Orders
        JPanel cardActiveJO = createSummaryCard(
                "Active Job Orders",
                activeJO,
                new Color(46, 204, 113),
                "Currently in progress"
        );
        cardActiveJO.setBounds(1040, 80, 300, 100);
        add(cardActiveJO);

        // Card 5: Completed Orders
        JPanel cardCompletedJO = createSummaryCard(
                "Completed Orders",
                completedJO,
                new Color(155, 89, 182),
                "Successfully finished"
        );
        cardCompletedJO.setBounds(50, 200, 300, 100);
        add(cardCompletedJO);

        // Card 6: Pending Orders
        JPanel cardPendingJO = createSummaryCard(
                "Pending Orders",
                JobOrderDao.getPendingJobOrders(),
                new Color(241, 196, 15),
                "Awaiting action"
        );
        cardPendingJO.setBounds(380, 200, 300, 100);
        add(cardPendingJO);

        // Card 7: On Hold Orders
        JPanel cardOnHoldJO = createSummaryCard(
                "On Hold Orders",
                JobOrderDao.getOnHoldJobOrders(),
                new Color(149, 165, 166),
                "Temporarily paused"
        );
        cardOnHoldJO.setBounds(710, 200, 300, 100);
        add(cardOnHoldJO);

        // Card 8: Total Orders
        JPanel cardTotalJO = createSummaryCard(
                "Total Job Orders",
                JobOrderDao.getTotalJobOrders(),
                new Color(52, 73, 94),
                "All time"
        );
        cardTotalJO.setBounds(1040, 200, 300, 100);
        add(cardTotalJO);
    }

    private void createJobOrderChart() {
        // Remove old chart if exists
        if (chartPanel != null) {
            remove(chartPanel);
        }

        chartPanel = new JPanel(null);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Job Order Stage Distribution",
                0, 0,
                new Font("SansSerif", Font.BOLD, 15),
                new Color(52, 73, 94)
        ));
        chartPanel.setBounds(50, 330, 650, 350);
        add(chartPanel);

        // Get stage counts
        int receiving = JobOrderDao.getJobOrdersByStage("Receiving");
        int dismantling = JobOrderDao.getJobOrdersByStage("Dismantling");
        int rebuilding = JobOrderDao.getJobOrdersByStage("Rebuilding");
        int painting = JobOrderDao.getJobOrdersByStage("Painting");
        int inspection = JobOrderDao.getJobOrdersByStage("Inspection");
        int completed = JobOrderDao.getJobOrdersByStage("Completed");

        // Create simple bar chart
        int chartY = 40;
        int barHeight = 35;
        int spacing = 10;

        addChartBar(chartPanel, "Receiving", receiving, new Color(52, 152, 219), chartY);
        chartY += barHeight + spacing;

        addChartBar(chartPanel, "Dismantling", dismantling, new Color(230, 126, 34), chartY);
        chartY += barHeight + spacing;

        addChartBar(chartPanel, "Rebuilding", rebuilding, new Color(241, 196, 15), chartY);
        chartY += barHeight + spacing;

        addChartBar(chartPanel, "Painting", painting, new Color(155, 89, 182), chartY);
        chartY += barHeight + spacing;

        addChartBar(chartPanel, "Inspection", inspection, new Color(52, 73, 94), chartY);
        chartY += barHeight + spacing;

        addChartBar(chartPanel, "Completed", completed, new Color(46, 204, 113), chartY);
    }

    private void addChartBar(JPanel panel, String label, int count, Color color, int y) {
        // Label
        JLabel lblStage = new JLabel(label);
        lblStage.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblStage.setBounds(20, y, 120, 30);
        panel.add(lblStage);

        // Bar
        int maxWidth = 450;
        int total = JobOrderDao.getTotalJobOrders();
        int barWidth = total > 0 ? (int) ((double) count / total * maxWidth) : 0;

        if (barWidth < 30 && count > 0) barWidth = 30; // Minimum visible width

        JPanel bar = new JPanel(null);
        bar.setBackground(color);
        bar.setBounds(150, y + 5, barWidth, 20);
        panel.add(bar);

        // Count label
        JLabel lblCount = new JLabel(String.valueOf(count));
        lblCount.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblCount.setForeground(Color.WHITE);
        lblCount.setBounds(barWidth > 40 ? 10 : barWidth + 5, 2, 50, 16);
        bar.add(lblCount);
    }

    private void createActivityLog() {
        // Remove old activity panel if exists
        if (activityPanel != null) {
            remove(activityPanel);
        }

        activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Recent Activity",
                0, 0,
                new Font("SansSerif", Font.BOLD, 15),
                new Color(52, 73, 94)
        ));
        activityPanel.setBounds(720, 330, 620, 350);
        add(activityPanel);

        // Table for activity log
        String[] columns = {"Date", "Action", "Details"};
        activityModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        activityTable = new JTable(activityModel);
        activityTable.setRowHeight(30);
        activityTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        activityTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        activityTable.getTableHeader().setBackground(new Color(52, 73, 94));
        activityTable.getTableHeader().setForeground(Color.WHITE);

        // Set column widths
        activityTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        activityTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        activityTable.getColumnModel().getColumn(2).setPreferredWidth(350);

        JScrollPane scrollPane = new JScrollPane(activityTable);
        activityPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void loadRecentActivities() {
        try {
            activityModel.setRowCount(0);

            List<Object[]> activities = JobOrderDao.getRecentActivities(10);

            if (activities.isEmpty()) {
                activityModel.addRow(new Object[]{
                        "-",
                        "No Activity",
                        "No recent activities to display"
                });
            } else {
                for (Object[] activity : activities) {
                    activityModel.addRow(activity);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            activityModel.addRow(new Object[]{
                    "-",
                    "Error",
                    "Failed to load activities: " + e.getMessage()
            });
        }
    }

    private void refreshDashboard() {
        // Only refresh the data, not the entire UI structure
        createSummaryCards();
        createJobOrderChart();
        loadRecentActivities();

        // Refresh the panel
        revalidate();
        repaint();

        JOptionPane.showMessageDialog(this,
                "Dashboard refreshed successfully!",
                "Refresh Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createSummaryCard(String title, int count, Color color, String subtitle) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTitle.setForeground(Color.DARK_GRAY);
        lblTitle.setBounds(10, 10, 280, 25);
        card.add(lblTitle);

        JLabel lblCount = new JLabel(String.valueOf(count));
        lblCount.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblCount.setForeground(color);
        lblCount.setBounds(10, 35, 280, 40);
        card.add(lblCount);

        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblSubtitle.setForeground(new Color(120, 120, 120));
        lblSubtitle.setBounds(10, 75, 280, 15);
        card.add(lblSubtitle);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return card;
    }
}