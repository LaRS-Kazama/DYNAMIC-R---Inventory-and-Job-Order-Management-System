package DaoClass;

import CommonConstant.MysqlCredentials;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobOrderDao {

    // ======================
    // DASHBOARD COUNTS
    // ======================

    public static int getActiveJobOrders() {
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_JOBORDER_TABLE +
                " WHERE stage IN ('Receiving', 'Dismantling', 'Rebuilding', 'Painting', 'Inspection')";
        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getCompletedJobOrders() {
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_JOBORDER_TABLE +
                " WHERE stage = 'Completed'";
        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    // ======================
    // ADD NEW JOB ORDER
    // ======================

    public static boolean addJobOrder(
            String clientName,
            String department,
            String employeeAssigned,
            java.sql.Date deadline,
            String stage,
            String status,
            String materials,
            String jobDescription,
            String customization,
            String notes,
            String referenceNo,
            String attachmentPath
    ) {
        String query = "INSERT INTO " + MysqlCredentials.DB_JOBORDER_TABLE +
                " (client_name, department, employee_assigned, deadline, stage, status, " +
                "materials, job_description, customization, notes, reference_no, attachment_path, date_created)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)";

        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, clientName);
            ps.setString(2, department);
            ps.setString(3, employeeAssigned);
            ps.setDate(4, deadline);
            ps.setString(5, stage);
            ps.setString(6, status);
            ps.setString(7, materials);
            ps.setString(8, jobDescription);
            ps.setString(9, customization);
            ps.setString(10, notes);
            ps.setString(11, referenceNo);
            ps.setString(12, attachmentPath);

            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows inserted: " + rowsAffected);  // Debug
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding job order:");
            e.printStackTrace();
            return false;
        }
    }


    // ======================
    // UPDATE JOB ORDER
    // ======================

    public static boolean updateJobOrder(
            String jobOrderId,
            String clientName,
            String department,
            String employee,
            java.sql.Date deadline,
            String stage,
            String status
    ) {
        String query = "UPDATE " + MysqlCredentials.DB_JOBORDER_TABLE + " SET " +
                "client_name = ?, department = ?, employee_assigned = ?, deadline = ?, " +
                "stage = ?, status = ? WHERE job_id = ?";

        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, clientName);
            ps.setString(2, department);
            ps.setString(3, employee);
            ps.setDate(4, deadline);
            ps.setString(5, stage);
            ps.setString(6, status);
            ps.setString(7, jobOrderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // ======================
    // DELETE JOB ORDER
    // ======================

    public static boolean deleteJobOrder(String jobOrderId) {
        String query = "DELETE FROM " + MysqlCredentials.DB_JOBORDER_TABLE + " WHERE job_id = ?";

        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, jobOrderId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // ======================
    // LOAD ALL JOB ORDERS (for JTable)
    // ======================

    public static List<Object[]> getAllJobOrders() {
        List<Object[]> list = new ArrayList<>();

        String query = "SELECT job_id, client_name, department, employee_assigned, deadline, " +
                "stage, status, materials, job_description, customization, notes, " +
                "reference_no, attachment_path, date_created FROM " +
                MysqlCredentials.DB_JOBORDER_TABLE + " ORDER BY date_created DESC";

        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Object[]{
                        false,                              // Checkbox column
                        rs.getString("job_id"),
                        rs.getString("client_name"),
                        rs.getString("department"),
                        rs.getString("employee_assigned"),
                        rs.getDate("deadline"),
                        rs.getString("stage"),
                        rs.getString("status"),
                        rs.getString("materials"),
                        rs.getString("job_description"),
                        rs.getString("customization"),
                        rs.getString("notes"),
                        rs.getString("reference_no"),
                        rs.getString("attachment_path"),
                        rs.getDate("date_created")
                });
            }

        } catch (SQLException e) {
            System.err.println("Error loading job orders:");
            e.printStackTrace();
        }

        return list;
    }

    // Add these methods to your JobOrderDao class

    // Get pending job orders count
    public static int getPendingJobOrders() {
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_JOBORDER_TABLE +
                " WHERE status = 'Pending'";
        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get on hold job orders count
    public static int getOnHoldJobOrders() {
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_JOBORDER_TABLE +
                " WHERE status = 'On Hold'";
        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get total job orders count
    public static int getTotalJobOrders() {
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_JOBORDER_TABLE;
        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get job orders by stage
    public static int getJobOrdersByStage(String stage) {
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_JOBORDER_TABLE +
                " WHERE stage = ?";
        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, stage);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get recent activities (last 10)
    public static List<Object[]> getRecentActivities(int limit) {
        List<Object[]> activities = new ArrayList<>();

        String query = "SELECT date_created, stage, CONCAT(job_id, ' - ', client_name, ' (', department, ')') AS details " +
                "FROM " + MysqlCredentials.DB_JOBORDER_TABLE +
                " ORDER BY date_created DESC LIMIT ?";

        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                activities.add(new Object[]{
                        rs.getDate("date_created").toString(),
                        "Job Order: " + rs.getString("stage"),
                        rs.getString("details")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activities;
    }

    // Get job orders by department (FIXED to include materials for warehouse)
    public static List<Object[]> getJobOrdersByDepartment(String department) {
        List<Object[]> list = new ArrayList<>();

        // FIXED: Added materials to SELECT for warehouse
        String query = "SELECT job_id, client_name, employee_assigned, deadline, " +
                "stage, status, materials, notes " +
                "FROM " + MysqlCredentials.DB_JOBORDER_TABLE +
                " WHERE department = ? ORDER BY job_id ASC";

        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, department);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("job_id"),
                        rs.getString("client_name"),
                        rs.getString("employee_assigned"),
                        rs.getDate("deadline"),
                        rs.getString("stage"),
                        rs.getString("status"),
                        rs.getString("materials"),  // ADDED for warehouse
                        rs.getString("notes")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Get count by department and status
    public static int getJobOrdersByDepartment(String department, String status) {
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_JOBORDER_TABLE +
                " WHERE department = ? AND status = ?";

        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, department);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get count by department and stage
    public static int getJobOrdersByDepartmentAndStage(String department, String stage) {
        String query = "SELECT COUNT(*) FROM " + MysqlCredentials.DB_JOBORDER_TABLE +
                " WHERE department = ? AND stage = ?";
        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, department);
            ps.setString(2, stage);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ======================
// UPDATE JOB ORDER PROGRESS (for Warehouse/Production Foreman)
// ======================

    public static boolean updateJobOrderProgress(String jobId, String stage, String status, String notes) {
        String query = "UPDATE " + MysqlCredentials.DB_JOBORDER_TABLE +
                " SET stage = ?, status = ?, notes = ? WHERE job_id = ?";

        try (Connection conn = DriverManager.getConnection(
                MysqlCredentials.DB_URL, MysqlCredentials.DB_USERNAME, MysqlCredentials.DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, stage);
            ps.setString(2, status);
            ps.setString(3, notes);
            ps.setString(4, jobId);

            int rowsAffected = ps.executeUpdate();
            System.out.println("Progress updated for job: " + jobId + ", rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating job order progress:");
            e.printStackTrace();
            return false;
        }
    }
}