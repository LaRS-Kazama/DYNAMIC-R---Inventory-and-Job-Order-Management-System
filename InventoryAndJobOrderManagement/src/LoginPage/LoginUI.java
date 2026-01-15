package LoginPage;

import AdminPage.AdminDashboard;
import CommonConstant.AppColors;
import DaoClass.LoginDAO;
import ProductionForemanPage.ProductionJobOrderDashboard;
import WarehouseManagerPage.WarehouseDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginUI extends BaseForm{

    public LoginUI() {
        super("Dynamic R Login");
        initComponents();
    }

    private void initComponents() {
        //Background
        ImageIcon bgImage = new ImageIcon(getClass().getResource("/images/background.jpg"));
        Image image = bgImage.getImage();
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // === MOVE BACKGROUND IMAGE HERE ===
                int imgWidth = image.getWidth(this);
                int imgHeight = image.getHeight(this);

                // adjust these values to reposition image
                int xOffset = 45; // move left (-), right (+)
                int yOffset = 150;  // move up (-), down (+)

                g.drawImage(image, xOffset, yOffset, imgWidth, imgHeight, this);
            }
        };
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(null);

        // Mask background color (matches dominant tone of image)
        getContentPane().setBackground(new Color(30, 30, 30)); // dark gray background blending with the image
        backgroundPanel.setBackground(new Color(30, 30, 30));

        //Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(AppColors.BORDER_GRAY);
        loginPanel.setBounds(850, 150, 450, 450);
        backgroundPanel.add(loginPanel);

        //Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.jpg"));
        Image scaledIcon = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaledIcon));

        logo.setBounds(165, 30, 120, 120);
        loginPanel.add(logo);

        //Title
        JLabel title = new JLabel("DYNAMIC R LOGIN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(AppColors.LIGHT_TEXT);
        title.setBounds(50, 160, 350, 40);
        loginPanel.add(title);

        //Username Field
        JTextField txtUsername = new JTextField();
        txtUsername.setBounds(75, 230, 300, 40);
        txtUsername.setBackground(AppColors.DARK_TEXT);
        txtUsername.setForeground(AppColors.LIGHT_TEXT);
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtUsername.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addPlaceHolderBehavior(txtUsername, "Username");
        loginPanel.add(txtUsername);

        //Password Field
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(75, 290, 300, 40);
        txtPassword.setBackground(AppColors.DARK_TEXT);
        txtPassword.setForeground(AppColors.LIGHT_TEXT);
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addPlaceHolderBehavior(txtPassword, "Password");
        loginPanel.add(txtPassword);

        //Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 360, 150, 45);
        btnLogin.setBackground(AppColors.PRIMARY_RED);
        btnLogin.setForeground(AppColors.LIGHT_TEXT);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD,18));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        loginPanel.add(btnLogin);

        // Hover effects for Login button
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                btnLogin.setBackground(AppColors.PRIMARY_RED_HOVER);
            }

            public void mouseExited(MouseEvent e){
                btnLogin.setBackground(AppColors.PRIMARY_RED);
            }
        });

        // Function of the login button
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            String role = LoginDAO.validLogin(username, password);

            if (role != null){
                JOptionPane.showMessageDialog(this, "Welcome, " + username + " (" + role + ")");

                dispose();

                //Redirect based on the role
                switch (role) {
                    case "Admin" -> new AdminDashboard().setVisible(true);
                    case "Warehouse Manager" -> new WarehouseDashboard().setVisible(true);
                    case "Production Foreman" -> new ProductionJobOrderDashboard().setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void addPlaceHolderBehavior(JTextField field, String placeholder){
        field.setText(placeholder);
        field.setForeground(new Color(150, 150,150));

        field.addFocusListener(new java.awt.event.FocusAdapter(){
            @Override
            public void focusGained(java.awt.event.FocusEvent e){
                if (field.getText().equals(placeholder)){
                    field.setText("");
                    field.setForeground(AppColors.LIGHT_TEXT);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e){
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(150, 150, 150));
                }
            }
        });
    }
}
