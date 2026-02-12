package database;

import model.Cashier;
import model.Manager;
import model.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    private Connection requireConnection() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new SQLException("Failed to obtain database connection.");
        }
        return conn;
    }

    public boolean addCashier(Cashier cashier) {
        String sql = "INSERT INTO staff (id, name, salary, role, register_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = requireConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cashier.getId());
            stmt.setString(2, cashier.getName());
            stmt.setDouble(3, cashier.getSalary());
            stmt.setString(4, "Cashier");
            stmt.setInt(5, cashier.getRegisterNumber());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding cashier: " + e.getMessage());
            return false;
        }
    }

    public boolean addManager(Manager manager) {
        String sql = "INSERT INTO staff (id, name, salary, role, team_size) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = requireConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, manager.getId());
            stmt.setString(2, manager.getName());
            stmt.setDouble(3, manager.getSalary());
            stmt.setString(4, "Manager");
            stmt.setInt(5, manager.getTeamSize());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding manager: " + e.getMessage());
            return false;
        }
    }

    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY id";

        try (Connection conn = requireConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                staffList.add(extractStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public Staff getStaffById(int id) {
        String sql = "SELECT * FROM staff WHERE id = ?";
        try (Connection conn = requireConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractStaff(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStaff(Staff staff) {
        final String sql;
        if (staff instanceof Cashier) {
            sql = "UPDATE staff SET name=?, salary=?, register_number=? WHERE id=?";
        } else if (staff instanceof Manager) {
            sql = "UPDATE staff SET name=?, salary=?, team_size=? WHERE id=?";
        } else {
            throw new IllegalArgumentException("Unsupported staff type: " + staff.getClass().getSimpleName());
        }

        try (Connection conn = requireConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, staff.getName());
            stmt.setDouble(2, staff.getSalary());

            if (staff instanceof Cashier) {
                stmt.setInt(3, ((Cashier) staff).getRegisterNumber());
            } else {
                stmt.setInt(3, ((Manager) staff).getTeamSize());
            }

            stmt.setInt(4, staff.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStaff(int id) {
        String sql = "DELETE FROM staff WHERE id = ?";
        try (Connection conn = requireConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Staff> searchByName(String name) {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE name ILIKE ? ORDER BY id";

        try (Connection conn = requireConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractStaff(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Staff> searchBySalaryRange(double min, double max) {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE salary BETWEEN ? AND ? ORDER BY salary DESC";

        try (Connection conn = requireConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, min);
            stmt.setDouble(2, max);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractStaff(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Staff> searchBySmallestSalary() {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE salary = (SELECT MIN(salary) FROM staff) ORDER BY id";

        try (Connection conn = requireConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(extractStaff(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Staff extractStaff(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double salary = rs.getDouble("salary");

        if ("Cashier".equalsIgnoreCase(role)) {
            return new Cashier(id, name, salary, rs.getInt("register_number"));
        }
        if ("Manager".equalsIgnoreCase(role)) {
            return new Manager(id, name, salary, rs.getInt("team_size"));
        }

        throw new SQLException("Unknown staff role '" + role + "' for staff id=" + id);
    }
}