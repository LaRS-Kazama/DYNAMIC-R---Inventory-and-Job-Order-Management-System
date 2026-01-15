package DaoClass;

import CommonConstant.MysqlCredentials;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDao {

    // ✅ Add new inventory item (with location, auto date)
    public static boolean addItem(String itemName, String category, int quantity,
                                  String batchNo, String location, String status) {
        String query = "INSERT INTO " + MysqlCredentials.DB_INVENTORY_TABLE_NAME +
                " (inventory_id, item_name, category, quantity, batch_no, location, status, date_added) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)";

        try (Connection connection = DriverManager.getConnection(
                MysqlCredentials.DB_URL,
                MysqlCredentials.DB_USERNAME,
                MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {

            String inventoryId = generateId();

            ps.setString(1, inventoryId);
            ps.setString(2, itemName);
            ps.setString(3, category);
            ps.setInt(4, quantity);
            ps.setString(5, batchNo);
            ps.setString(6, location);
            ps.setString(7, status);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Generate new inventory ID (like INV-001)
    public static String generateId() {
        String prefix = "INV-";
        String query = "SELECT inventory_id FROM " + MysqlCredentials.DB_INVENTORY_TABLE_NAME +
                " ORDER BY inventory_id DESC LIMIT 1";

        try (Connection connection = DriverManager.getConnection(
                MysqlCredentials.DB_URL,
                MysqlCredentials.DB_USERNAME,
                MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            int nextId = 1;
            if (rs.next()) {
                String lastId = rs.getString("inventory_id");
                try {
                    nextId = Integer.parseInt(lastId.replace(prefix, "")) + 1;
                } catch (NumberFormatException ignored) {}
            }
            return prefix + String.format("%03d", nextId);
        } catch (SQLException e) {
            e.printStackTrace();
            return prefix + "001";
        }
    }

    // ✅ Update existing item (with location)
    public static boolean updateItem(String itemId, String itemName, String category,
                                     int quantity, String batchNo, String location, String status) {
        String query = "UPDATE " + MysqlCredentials.DB_INVENTORY_TABLE_NAME +
                " SET item_name=?, category=?, quantity=?, batch_no=?, location=?, status=? " +
                "WHERE inventory_id=?";

        try (Connection connection = DriverManager.getConnection(
                MysqlCredentials.DB_URL,
                MysqlCredentials.DB_USERNAME,
                MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, itemName);
            ps.setString(2, category);
            ps.setInt(3, quantity);
            ps.setString(4, batchNo);
            ps.setString(5, location);
            ps.setString(6, status);
            ps.setString(7, itemId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getAllItemNames() {
        List<String> itemsName = new ArrayList<>();
        String query = "SELECT item_name FROM " + MysqlCredentials.DB_INVENTORY_TABLE_NAME +
                " ORDER BY item_name ASC";

        try (Connection con = DriverManager.getConnection(
                MysqlCredentials.DB_URL,
                MysqlCredentials.DB_USERNAME,
                MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                itemsName.add(rs.getString("item_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemsName;
    }

    // ✅ Fetch all inventory items for JTable (with correct column order)
    public static List<Object[]> getAllItems() {
        List<Object[]> dataInventory = new ArrayList<>();
        String query = "SELECT inventory_id, item_name, category, quantity, " +
                "batch_no, location, status, date_added FROM " +
                MysqlCredentials.DB_INVENTORY_TABLE_NAME +
                " ORDER BY date_added DESC";

        try (Connection connection = DriverManager.getConnection(
                MysqlCredentials.DB_URL,
                MysqlCredentials.DB_USERNAME,
                MysqlCredentials.DB_PASSWORD);
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                dataInventory.add(new Object[]{
                        false,                          // Checkbox
                        rs.getString("inventory_id"),   // Item ID
                        rs.getString("item_name"),      // Item Name
                        rs.getString("category"),       // Category
                        rs.getInt("quantity"),          // Quantity
                        "pcs",                          // Unit (hardcoded or add to DB)
                        rs.getString("status"),         // Status
                        rs.getString("batch_no"),       // Batch No.
                        rs.getString("location"),       // Location
                        rs.getDate("date_added")        // Date Added
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataInventory;
    }

    // Methods for dashboard inventory DAO

    //===== Summary Counts =====
    public static int getTotalItems(){
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_INVENTORY_TABLE_NAME;

        try (Connection connection = DriverManager.getConnection(
                MysqlCredentials.DB_URL,
                MysqlCredentials.DB_USERNAME,
                MysqlCredentials.DB_PASSWORD);
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)){
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getLowStockCount() {
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_INVENTORY_TABLE_NAME +
                " WHERE quantity <= 5";
        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL,
                MysqlCredentials.DB_USERNAME,
                MysqlCredentials.DB_PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getDamagedCount() {
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_INVENTORY_TABLE_NAME +
                " WHERE status IN ('Damaged', 'Returned', 'Scrapped')";
        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL,
                MysqlCredentials.DB_USERNAME,
                MysqlCredentials.DB_PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ✅ Get available items only (excludes scrapped/damaged/returned items for job orders)
    public static List<String> getAvailableItemNamesForJobOrder() {
        List<String> itemsName = new ArrayList<>();
        String query = "SELECT item_name FROM " + MysqlCredentials.DB_INVENTORY_TABLE_NAME +
                " WHERE status NOT IN ('Scrapped', 'Damaged', 'Returned', 'Out of Stock') " +
                "ORDER BY item_name ASC";

        try (Connection con = DriverManager.getConnection(
                MysqlCredentials.DB_URL,
                MysqlCredentials.DB_USERNAME,
                MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                itemsName.add(rs.getString("item_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemsName;
    }

}