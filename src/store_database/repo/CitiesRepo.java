package store_database.repo;
import store_database.common.ConnectionManager;
import store_database.entity.Cities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitiesRepo {
    // Create
    public boolean insertCity(Cities cities) throws SQLException, ClassNotFoundException {
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "INSERT INTO cities (name) VALUES ('" +
                    cities.getName() + "');";
            System.out.println("Executing query: " + query);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println("Rows affected: " + rowsAffected);
            return (rowsAffected > 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Read (Single Cities by ID)
    public Cities selectCityById(int id) throws ClassNotFoundException, SQLException {
        Cities cities = null;
        cities = new Cities();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM cities WHERE id = " + id;
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                cities.setId(rs.getInt("id"));
                cities.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(" we cannot find your cities in the table " + e.getMessage());
            e.printStackTrace();
        }
        return cities;
    }

    // Read All
    public List<Cities> selectAllCities() throws ClassNotFoundException, SQLException {
        List<Cities> cities = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM cities";
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Cities city = new Cities();
                city.setId(rs.getInt("id"));
                city.setName(rs.getString("name"));
                cities.add(city);
            }
        } catch (SQLException e) {
            System.out.println(" we cannot not get all rows in the cities table " + e.getMessage());
            e.printStackTrace();
        }
        return cities;
    }

    // Delete
    public boolean deleteCity(int id) {
        boolean isDeleted = false;
        try {
            try (
                    Connection conn = ConnectionManager.getConnection();
                    Statement stmt = conn.createStatement();
            ) {
                String query = "DELETE FROM cities WHERE id = " + id;
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

    // Update Cities
    public boolean updateCity(Cities cities) throws ClassNotFoundException, SQLException {
        boolean flag = false; // Initialize flag outside try block

        try {
            // Get connection and statement in try-with-resources
            try (Connection conn = ConnectionManager.getConnection();
                 Statement stmt = conn.createStatement()) {

                String query = "UPDATE cities SET " +
                        "name = '" + cities.getName() + "' " +
                        "WHERE id = '" + cities.getId() + "'";
                System.out.println("Executing query: " + query);

                int noAffectedRows = stmt.executeUpdate(query);
                flag = noAffectedRows > 0; // Update flag based on update success
                System.out.println("Rows affected: " + noAffectedRows);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update cities: " + e.getMessage());
        }

        return flag; // Return flag value modified inside try block
    }

    public void printingAllCitiesDetails() {
        List<Cities> cities;

        try {
            cities = selectAllCities();
            if (cities != null && !cities.isEmpty()) {
                for (Cities city : cities) {
                    System.out.print(" | ID: " + city.getId());
                    System.out.print(" | Name: " + city.getName());
                    System.out.println();
                }

            } else {
                System.out.println(" we didn't find any city");
            }

        } catch (SQLException e) {
            System.out.println("database error simple Error ");
        } catch (ClassNotFoundException e) {
            System.out.println("we are not able to insert Row ");
            System.out.println(e.getMessage());
        }
    }
}



class TestCity{

    public static void main(String[]args){
        CitiesRepo citiesRepo=new CitiesRepo();

        Cities cities =new Cities();
        Cities cities1 =new Cities();
        Cities cities2 =new Cities();
        Cities cities3 =new Cities();

        cities.setId(1);
        cities1.setId(2);
        cities2.setId(3);
        cities3.setId(4);


        cities.setName("first Cities ");
        cities1.setName("second Cities ");
        cities2.setName("Third Cities ");
        cities3.setName("Fourth Cities ");

        try{
            citiesRepo.insertCity(cities);
            citiesRepo.insertCity(cities1);
            citiesRepo.insertCity(cities2);
            citiesRepo.insertCity(cities3);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


}
