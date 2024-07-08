package store_database.repo;


import store_database.common.ConnectionManager;
import store_database.entity.Size;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SizesRepo {
    // Create
    public boolean insertSize(Size size) throws SQLException, ClassNotFoundException {
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "INSERT INTO sizes (name) VALUES ('" +
                    size.getName() + "');";
            System.out.println("Executing query: " + query);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println("Rows affected: " + rowsAffected);
            return (rowsAffected > 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Read (Single Size by ID)
    public Size selectSizeById(int id) throws ClassNotFoundException, SQLException {
        Size size = null;
        size = new Size();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM sizes WHERE id = " + id;
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                size.setId(rs.getInt("id"));
                size.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(" we cannot find your size in the table " + e.getMessage());
            e.printStackTrace();
        }
        return size;
    }

    // Read All
    public List<Size> selectAllSizes() throws ClassNotFoundException, SQLException {
        List<Size> sizes = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM sizes";
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Size size = new Size();
                size.setId(rs.getInt("id"));
                size.setName(rs.getString("name"));
                sizes.add(size);
            }
        } catch (SQLException e) {
            System.out.println(" we cannot not get all rows in the sizes table " + e.getMessage());
            e.printStackTrace();
        }
        return sizes;
    }

    // Delete
    public boolean deleteSize(int id) {
        boolean isDeleted = false;
        try {
            try (
                    Connection conn = ConnectionManager.getConnection();
                    Statement stmt = conn.createStatement();
            ) {
                String query = "DELETE FROM sizes WHERE id = " + id;
                System.out.println(query);
                int noAffectedRows = stmt.executeUpdate(query);
                if (noAffectedRows == 1) {
                    System.out.println("One row deleted");
                    isDeleted = noAffectedRows != 0;  // You may don't need this put used to make sure not more
                } else {
                    System.out.println("NO Rows Affected");
                }

            } catch (SQLException e) {
                System.out.println(" we cannot delete your Row from the table ");
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return isDeleted;
    }

    // Update Size
    public boolean updateSize(Size size) throws ClassNotFoundException, SQLException {
        boolean flag = false; // Initialize flag outside try block

        try {
            // Get connection and statement in try-with-resources
            try (Connection conn = ConnectionManager.getConnection();
                 Statement stmt = conn.createStatement()) {

                String query = "UPDATE sizes SET " +
                        "name = '" + size.getName() + "' " +
                        "WHERE id = '" + size.getId() + "'";
                System.out.println("Executing query: " + query);

                int noAffectedRows = stmt.executeUpdate(query);
                flag = noAffectedRows > 0; // Update flag based on update success
                System.out.println("Rows affected: " + noAffectedRows);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update size: " + e.getMessage());
        }

        return flag; // Return flag value modified inside try block
    }

    public void printingAllSizesDetails() {
        List<Size> sizes;

        try {
            sizes = selectAllSizes();
            if (sizes != null && !sizes.isEmpty()) {
                for (Size size : sizes) {
                    System.out.print(" | ID: " + size.getId());
                    System.out.print(" | Name: " + size.getName());
                    System.out.println();
                }

            } else {
                System.out.println(" we didn't find any size");
            }

        } catch (SQLException e) {
            System.out.println("database error simple Error ");
        } catch (ClassNotFoundException e) {
            System.out.println("we are not able to insert Row ");
            System.out.println(e.getMessage());
        }
    }
}


class TestSizes {
    public static void main(String[] args) {
        SizesRepo sizesRepo = new SizesRepo();
        Size size = new Size();
        size.setId(1);
        size.setName("First size Mohamed Alaa amer ");
        Size size1 = new Size();
        size1.setId(2);
        size1.setName("second size ");

        try {
            sizesRepo.insertSize(size);
            sizesRepo.insertSize(size1);
        } catch (SQLException | ClassNotFoundException e) {
            e.getMessage();
        }

//
//        boolean isDeleted = categoryRepo.deleteSize(3);
//
//        System.out.println("This is the value of main function : " + isDeleted);
//        if (isDeleted) System.out.println("deleted Row ");
//        else System.out.println("Row is not deleted ");
//        categoryRepo.printingAllSizesDetails();
    }
}
