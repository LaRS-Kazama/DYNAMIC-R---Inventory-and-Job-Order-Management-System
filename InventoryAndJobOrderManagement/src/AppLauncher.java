import AdminPage.AdminDashboard;
import AdminPage.DashboardPanel;
import LoginPage.LoginUI;
import javax.swing.*;

public class AppLauncher{
    public static void main (String [] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginUI()  .setVisible(true);
            }
        });
    }
}