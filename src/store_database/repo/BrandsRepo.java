package store_database.repo;

import store_database.common.ConnectionManager;
import store_database.entity.Brands;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrandsRepo {
    // Create
    public boolean insertBrand(Brands brands) throws SQLException, ClassNotFoundException {
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "INSERT INTO brands (name, logo_path) VALUES ('" +
                    brands.getName() + "', '" + brands.getLogo_path() + "');";
            System.out.println("Executing query: " + query);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println("Rows affected: " + rowsAffected);
            return (rowsAffected > 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Read (Single Brands by ID)
    public Brands selectBrandById(int id) throws ClassNotFoundException, SQLException {
        Brands brands = null;
        brands = new Brands();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM brands WHERE id = " + id;
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                brands.setId(rs.getInt("id"));
                brands.setName(rs.getString("name"));
                brands.setLogo_path(rs.getString("logo_path"));
            }
        } catch (SQLException e) {
            System.out.println(" we cannot find your brands in the table " + e.getMessage());
            e.printStackTrace();
        }
        return brands;
    }

    // Read All
    public List<Brands> selectAllBrands() throws ClassNotFoundException, SQLException {
        List<Brands> brands = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM brands";
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Brands brand = new Brands();
                brand.setId(rs.getInt("id"));
                brand.setName(rs.getString("name"));
                brand.setLogo_path(rs.getString("logo_path"));
                brands.add(brand);
            }
        } catch (SQLException e) {
            System.out.println(" we cannot not get all rows in the brands table " + e.getMessage());
            e.printStackTrace();
        }
        return brands;
    }

    // Delete
    public boolean deleteBrand(int id) {
        boolean isDeleted = false;
        try {
            try (
                    Connection conn = ConnectionManager.getConnection();
                    Statement stmt = conn.createStatement();
            ) {
                String query = "DELETE FROM brands WHERE id = " + id;
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

    // Update Brands
    public boolean updateBrand(Brands brands) throws ClassNotFoundException, SQLException {
        boolean flag = false; // Initialize flag outside try block

        try {
            // Get connection and statement in try-with-resources
            try (Connection conn = ConnectionManager.getConnection();
                 Statement stmt = conn.createStatement()) {

                String query = "UPDATE brands SET " +
                        "name = '" + brands.getName() + "', " +
                        "logo_path = '" + brands.getLogo_path() + "' " +
                        "WHERE id = '" + brands.getId() + "'";
                System.out.println("Executing query: " + query);

                int noAffectedRows = stmt.executeUpdate(query);
                flag = noAffectedRows > 0; // Update flag based on update success
                System.out.println("Rows affected: " + noAffectedRows);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update brands: " + e.getMessage());
        }

        return flag; // Return flag value modified inside try block
    }

    public void printingAllBrandsDetails() {
        List<Brands> brands;

        try {
            brands = selectAllBrands();
            if (brands != null && !brands.isEmpty()) {
                for (Brands brand : brands) {
                    System.out.print(" | ID: " + brand.getId());
                    System.out.print(" | Name: " + brand.getName());
                    System.out.print(" | Logo Path : " + brand.getLogo_path());
                    System.out.println();
                }

            } else {
                System.out.println(" we didn't find any brand");
            }

        } catch (SQLException e) {
            System.out.println("database error simple Error ");
        } catch (ClassNotFoundException e) {
            System.out.println("we are not able to insert Row ");
            System.out.println(e.getMessage());
        }
    }
}


class TestBrand {

    public static void main(String[] args) {

        BrandsRepo brandsRepo = new BrandsRepo();

        Brands brands = new Brands();
        Brands brands1 = new Brands();
        Brands brands2 = new Brands();
        Brands brands3 = new Brands();
        brands.setId(1);
        brands.setName("first brands");
        brands.setLogo_path("mohamed/path/imageOne");

        brands1.setId(2);
        brands1.setName("second brands");
        brands1.setLogo_path("mohamed/path/imageTwo");


        brands2.setId(3);
        brands2.setName("Thirs brands");
        brands2.setLogo_path("mohamed/path/imageThree");

        try {
            brandsRepo.insertBrand(brands);
            brandsRepo.insertBrand(brands1);
            brandsRepo.insertBrand(brands2);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}