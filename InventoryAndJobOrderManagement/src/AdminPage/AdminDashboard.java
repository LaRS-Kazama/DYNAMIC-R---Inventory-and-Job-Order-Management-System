package AdminPage;

import LoginPage.BaseForm;
import LoginPage.LoginUI;
import CommonConstant.AppColors;
import LoginPage.LoginUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminDashboard extends BaseForm {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public AdminDashboard() {
        super("DYNAMIC R ADMIN PANEL");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        adminComponents();
    }

    private void adminComponents() {
        setLayout(new BorderLayout());

        // Sidebar (Navigation)
        JPanel sideBar = new JPanel();
        sideBar.setPreferredSize(new Dimension(180, getHeight()));
        sideBar.setBackground(AppColors.DARK_BACKGROUND);
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));

        sideBar.add(Box.createVerticalStrut(40));

        JButton btnDashboard = makeSideBarButton("Dashboard");
        JButton btnInventory = makeSideBarButton("Inventory");
        JButton btnJobOrder = makeSideBarButton("Job Order");
        JButton btnLogOut = makeSideBarButton("Log Out");

        sideBar.add(btnDashboard);
        sideBar.add(Box.createVerticalStrut(10));
        sideBar.add(btnInventory);
        sideBar.add(Box.createVerticalStrut(10));
        sideBar.add(btnJobOrder);
        sideBar.add(Box.createVerticalStrut(10));

        // Add spacer to push logout to bottom
        sideBar.add(Box.createVerticalGlue());

        // Logout button with different styling
        btnLogOut.setBackground(new Color(231, 76, 60)); // Red color for logout
        sideBar.add(btnLogOut);
        sideBar.add(Box.createVerticalStrut(20));

        add(sideBar, BorderLayout.WEST);

        // Content area (Card Layout)
        cardLayout = new CardLayout();
        contentPanel = new JPanel();
        contentPanel.setLayout(cardLayout);

        contentPanel.add(new DashboardPanel(), "Dashboard");
        contentPanel.add(new InventoryPanel(), "Inventory");
        contentPanel.add(new JobOrderPanel(), "Job Order");

        add(contentPanel, BorderLayout.CENTER);

        // Button Actions
        btnDashboard.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "Dashboard"));
        btnInventory.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "Inventory"));
        btnJobOrder.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "Job Order"));
        btnLogOut.addActionListener((ActionEvent e) -> logout());

        cardLayout.show(contentPanel, "Dashboard");
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Close current window
            this.dispose();

            // Open login form
            SwingUtilities.invokeLater(() -> {
                LoginUI loginForm = new LoginUI();
                loginForm.setVisible(true);
            });
        }
    }

    private JButton makeSideBarButton(String text) {
        JButton adminButton = new JButton(text);
        adminButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        adminButton.setForeground(AppColors.LIGHT_TEXT);
        adminButton.setBackground(new Color(35, 35, 35));
        adminButton.setFocusPainted(false);
        adminButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        adminButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminButton.setMaximumSize(new Dimension(160, 45));

        adminButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // Special hover color for logout button
                if (text.equals("Log Out")) {
                    adminButton.setBackground(new Color(192, 57, 43));
                } else {
                    adminButton.setBackground(new Color(55, 55, 55));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                // Restore original color
                if (text.equals("Log Out")) {
                    adminButton.setBackground(new Color(231, 76, 60));
                } else {
                    adminButton.setBackground(new Color(35, 35, 35));
                }
            }
        });
        return adminButton;
    }
}