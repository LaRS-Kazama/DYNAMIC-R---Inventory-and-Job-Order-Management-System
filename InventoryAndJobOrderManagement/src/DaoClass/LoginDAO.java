package DaoClass;


import CommonConstant.MysqlCredentials;

import java.security.MessageDigest;
import java.sql.*;

public class LoginDAO {
    // This methods is to hash password using SHA-256
    public static String hashPassword(String userPassword){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(userPassword.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // This checks if the user is already exist in the table
    public static boolean checkUser(String username, String userPassword){
        try{
            Connection connection = DriverManager.getConnection(MysqlCredentials.DB_URL,
                    MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);

            PreparedStatement checkUser = connection.prepareStatement(
                    "SELECT * FROM " + MysqlCredentials.DB_USERS_TABLE_NAME + " WHERE username = ?"
            );
            checkUser.setString(1, username);
            checkUser.setString(2, userPassword);

            ResultSet resultSet = checkUser.executeQuery();

            return resultSet.isBeforeFirst();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Validate login by checking user from the database
    public static String validLogin(String username, String userPassword) {
        try (Connection connection = DriverManager.getConnection(
                MysqlCredentials.DB_URL,
                MysqlCredentials.DB_USERNAME,
                MysqlCredentials.DB_PASSWORD)) {

            PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM " + MysqlCredentials.DB_USERS_TABLE_NAME + " WHERE username = ?"
            );
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("user_password");
                String inputHash = hashPassword(userPassword); // Hash the input password

                if (storedHash.equalsIgnoreCase(inputHash)) {
                    return rs.getString("role"); // Success → return the role
                } else {
                    return null; // Hash doesn’t match
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User not found
    }

}
