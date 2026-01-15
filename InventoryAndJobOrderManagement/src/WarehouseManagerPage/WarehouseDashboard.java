package WarehouseManagerPage;

import LoginPage.BaseForm;
import LoginPage.LoginUI;
import CommonConstant.AppColors;
import LoginPage.LoginUI;
import WarehouseManagerPage.WarehouseJobOrderPanel;

import javax.swing.*;
import java.awt.*;

public class WarehouseDashboard extends BaseForm {
    public WarehouseDashboard() {
        super("DYNAMIC R WAREHOUSE MANAGER PANEL");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sideBar = new JPanel();
        sideBar.setPreferredSize(new Dimension(200, getHeight()));
        sideBar.setBackground(AppColors.DARK_BACKGROUND);
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));

        sideBar.add(Box.createVerticalStrut(40));

        JButton btnJobOrders = makeSideBarButton("Job Orders");
        JButton btnLogOut = makeSideBarButton("Log Out");

        sideBar.add(btnJobOrders);
        sideBar.add(Box.createVerticalStrut(10));

        // Add spacer to push logout to bottom
        sideBar.add(Box.createVerticalGlue());

        // Logout button with red styling
        btnLogOut.setBackground(new Color(231, 76, 60));
        sideBar.add(btnLogOut);
        sideBar.add(Box.createVerticalStrut(20));

        add(sideBar, BorderLayout.WEST);

        // Content area
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(new WarehouseJobOrderPanel(), BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // Button Actions
        btnJobOrders.addActionListener(e -> {
            // Already showing job orders panel
        });

        btnLogOut.addActionListener(e -> logout());

        revalidate();
        repaint();
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
            this.dispose();

            SwingUtilities.invokeLater(() -> {
                LoginUI loginForm = new LoginUI();
                loginForm.setVisible(true);
            });
        }
    }

    private JButton makeSideBarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setForeground(AppColors.LIGHT_TEXT);
        button.setBackground(new Color(35, 35, 35));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 45));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (text.equals("Log Out")) {
                    button.setBackground(new Color(192, 57, 43));
                } else {
                    button.setBackground(new Color(55, 55, 55));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (text.equals("Log Out")) {
                    button.setBackground(new Color(231, 76, 60));
                } else {
                    button.setBackground(new Color(35, 35, 35));
                }
            }
        });
        return button;
    }
}