package store_database.repo;

import store_database.entity.Categories;
import store_database.common.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
----------------- Repo | Repository | repositories for all Classes in java --------------
 * If you have 12 table in the database
 * you need 12 DTO | BOJO | DTO , 12 class and single ConnectionManager

 Layers that we have
   [1- Data Model
   [2- Data Access layer
   [2- business layer
   [3- presentation layer
 *
 * */

/*
*
*
* ============= You need to know these layers uaAlaa ======================
* +-------------------+
|  Presentation     |   (HTML, CSS, JavaScript, React)
+-------------------+
|  Application      |   (Spring Boot, Controllers, Services)
+-------------------+
|  Domain           |   (Business Logic, Domain Models)
+-------------------+
|  Persistence      |   (JPA Repositories, DAOs)
+-------------------+
|  Database         |   (MySQL, PostgreSQL)+-------------------+
|  Integration      |   (REST APIs, SOAP Services)
+-------------------+
|  Security         |   (Spring Security, OAuth)
+-------------------+
|  Caching          |   (Redis, Memcached)
+-------------------+
|  Logging &        |   (Log4j, ELK Stack, Prometheus)
|  Monitoring       |
+-------------------+

*
*
*
* */

public class CategoryRepo {

    // We are going to use prepared Statements uaAmer
    // we need to make all CRUD operations using prepared Statements uaAlaa
    public int insertCategoryPreparedStatement(Categories categories) throws SQLException, ClassNotFoundException {

        int noAffectedRows;
        Connection conn = ConnectionManager.getConnection();
        String query = "INSERT INTO categories (id, name, description) VALUES (?, ?, ?)";

        PreparedStatement prstmt = conn.prepareStatement(query);
        prstmt.setInt(1, categories.getId());
        prstmt.setString(2, categories.getName());
        prstmt.setString(3, categories.getDescription());

        noAffectedRows = prstmt.executeUpdate();
        System.out.println("Number of Rows affected are: " + noAffectedRows);
        return noAffectedRows;
    }


    public boolean updatePreparedStatement(Categories categories) {

        try (Connection conn = ConnectionManager.getConnection();
        ) {
            String sql = "UPDATE categories SET name = ? , description = ? WHERE id = ? ";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Set the parameters
            pstmt.setString(1, categories.getName());
            pstmt.setString(2, categories.getDescription());
            pstmt.setInt(3, categories.getId());

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("There is an erro in this function solve it right now ");
//            e.printStackTrace();
        }
        return false;
    }


    public int updateCategoryPreparedStatement(Categories categories) {

//        String sqlQuery = "UPDATE categories SET name = ? , description=? WHERE id = ? ";
        String sqlQuery = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        int noAffectedRows = 0;

        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement prstmt = conn.prepareStatement(sqlQuery);
        ) {
            prstmt.setString(1, categories.getName());
            prstmt.setString(2, categories.getDescription());
            prstmt.setInt(3, categories.getId());


            noAffectedRows = prstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("sql Error uaAmer you need to solve this mission");
            e.getMessage();

        }
        return noAffectedRows;
    }


    //--------------------------------------------------------------------------------------------------
// Create
    public boolean insertCategory(Categories categories) throws SQLException, ClassNotFoundException {
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "INSERT INTO categories (name, description) VALUES ('" +
                    categories.getName() + "', '" + categories.getDescription() + "');";
            System.out.println("Executing query: " + query);
            int rowsAffected = stmt.executeUpdate(query);
            System.out.println("Rows affected: " + rowsAffected);
            return (rowsAffected > 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Read   used to read Single Categories By id
    public Categories selectCategoryById(int id) throws ClassNotFoundException, SQLException {
        Categories categories = null;
        categories = new Categories();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM categories WHERE id = " + id;
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                categories.setId(rs.getInt("id"));
                categories.setName(rs.getString("name"));
                categories.setDescription(rs.getString("description"));

            }
        } catch (SQLException e) {
            System.out.println(" we cannot find you categories in the table " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }

    // Read All
    public List<Categories> selectAllCategories() throws ClassNotFoundException, SQLException {
        List<Categories> categories = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM categories";
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Categories category = new Categories();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println(" we cannot not get all rows in the categories table " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }

    //Delete
    public boolean deleteCategory(int id) {
        boolean isDeleted = false;
        try {
            try (
                    Connection conn = ConnectionManager.getConnection();
                    Statement stmt = conn.createStatement();
            ) {
                String query = "DELETE FROM categories WHERE id = " + id;
                System.out.println(query);
                int noAffectedRows = stmt.executeUpdate(query);
                if (noAffectedRows == 1) {
                    System.out.println("One row deleted uaamer ");
                    isDeleted = noAffectedRows != 0;  // You may don't need this put used to make sure not more
                } else {
                    System.out.println("NO Rows Affected uaamer ");
                }

            } catch (SQLException e) {
                System.out.println(" we cannot delete you Row from the table ");
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return isDeleted;
    }

// Update Categories uaAlaa

    public boolean updateCategory(Categories categories) throws ClassNotFoundException, SQLException {
        boolean flag = false; // Initialize flag outside try block
        // to use this variable you need to make try inside another try uaAmer

        try {
            // Get connection and statement in try-with-resources
            try (Connection conn = ConnectionManager.getConnection();
                 Statement stmt = conn.createStatement()) {

                String query = "UPDATE categories SET " +
                        "name = '" + categories.getName() + "', " +
                        "description = '" + categories.getDescription() + "' " +
                        "WHERE id = '" + categories.getId() + "'";
                System.out.println("Executing query: " + query);

                int noAffectedRows = stmt.executeUpdate(query);
                flag = noAffectedRows > 0; // Update flag based on update success
                System.out.println("Rows affected: " + noAffectedRows);
            }
        } catch (SQLException e) {
            System.out.println("Failed to update categories: " + e.getMessage());
        }

        return flag; // Return flag value modified inside try block
    }

    public void printingAllCategoriesDetails() {
        List<Categories> categories;

        try {
            categories = selectAllCategories();
            if (categories != null && !categories.isEmpty()) {
                for (Categories category : categories) {
                    System.out.print(" | ID: " + category.getId());
                    System.out.print(" | Name: " + category.getName());
                    System.out.print(" | Description : " + category.getDescription());
                    System.out.println();
                }

            } else {
                System.out.println(" we didn't find any category");
            }

        } catch (SQLException e) {
            System.out.println("database error simple Error ");
        } catch (ClassNotFoundException e) {
            System.out.println("we are not able to insert Row uaAlaa  ");
            System.out.println(e.getMessage());
        }
    }

}


class Test {
    public static void main(String[] args) {



        Categories categories1 = new Categories();
        categories1.getClass();

        System.out.println(categories1.getClass().getSimpleName()); // This will bring the name of the class in java Code
        // And you need to get the lowercase to bring the table name


//        categories1.setId(1);
//        categories1.setName("Updated values via prepared Statements Thanks very much  ");
//        categories1.setDescription("This is the coming mission what is the generic programming ");
//
//
//        CategoryRepo categoryRepo = new CategoryRepo();
//
//        boolean result = categoryRepo.updatePreparedStatement(categories1);
//
//        System.out.println("Number of Rows affected is : " + result);
//
//        try {
//            int rowsAffected = categoryRepo.insertCategoryPreparedStatement(category);
//            if (rowsAffected != 0) System.out.println("Rows affected !!!");
//        } catch (SQLException e) {
//            System.out.println(" sql error uaAmer ");
////            throw new RuntimeException(e);
//        } catch (ClassNotFoundException e) {
//            System.out.println(" class not found exception uaAmer ");
////            throw new RuntimeException(e);
//        }

//        What is the generic programming uaAmer
//        System.out.println(Categories.class.getName());
//        System.out.println(Categories.class.getSuperclass());
//
//        Object object = new Object();
//        System.out.println(object.getClass());
//
//        CategoryRepo categoryRepo = new CategoryRepo();
//        boolean isDeleted = categoryRepo.deleteCategory(3);
//
//        System.out.println("This is the value of main function : " + isDeleted);
//        if (isDeleted) System.out.println("deleted Row ");
//        else System.out.println("Row is not deleted ");
//        categoryRepo.printingAllCategoriesDetails();
    }
}


// Categories DTO data transfer object | DAO DataAccessObject
// via this class you could access the table in database
// DAO data access Object
// Model ==> Repo used to access data in database
// class OldCategoryRepo {
//
//    /*
//     * DTO = entity = BOJO = class = data Transfer Object [ This CategoryRepo]
//     *
//     * */
//
//    // This will be for the class ==> and you need to make this for every table
//    ConnectionManager connectionManager = new ConnectionManager();
//
//    // They are five function CRUD OPERATIONS
//    public void insert(Categories category) {
//        // Every function of the CRUD operation will use the Object from this class uaAlaa
//        try (
//                Connection connection = connectionManager.connect();
//                Statement statement = connection.createStatement();
//        ) {
//            String query = "INSERT INTO categories (name, description) VALUES ('" +
//                    category.getName() + "', '" + category.getDescription() + "');";
//            System.out.println("Executing query: " + query);
//
//            int rowsAffected = statement.executeUpdate(query);
//            if (rowsAffected == 1) {
//                System.out.println("one row affected ");
//            } else {
//                System.out.println("No Rows affected");
//            }
//
//        } catch (SQLException e) {
//            System.out.println("We cannot insert Categories to the database : " + e.getMessage());
//        }
//
//
//    }
//
//    public void update(Categories category) {
//        // This is the best way yaAlaa to make connection
//        try (
//                Connection connection = connectionManager.connect();
//                Statement statement = connection.createStatement();
//        ) {
//            /*
//             * This is very essential Tool to check if the driver related to
//             * mysql connector is exist or not
//             * */
//
//            try {
//                String query = "INSERT INTO categories (name, description) VALUES ('" +
//                        category.getName() + "', '" + category.getDescription() + "');";
//                System.out.println("Executing query: " + query);
//
//                int noAffectedRows = statement.executeUpdate(query);  // used to run the command and update the values by inserting in DB
//                /*
//                 * To check if the query inserted correctly of not uaAmer
//                 * */
//                int rowsAffected = statement.executeUpdate(query);
//                System.out.println("Rows affected: " + rowsAffected);
//
//            } catch (SQLException e) {
//                System.out.println("We failed to connect to database: " + e.getMessage());
//            }
//        } catch (SQLException e) {
//            System.out.println("we Cannot insert Categories to you database ");
//        }
//    }
//
//
//    public Categories select(String id) {
//        Categories currentCategory = new Categories();
//        try (
//                Connection connection = connectionManager.connect();
//                Statement statement = connection.createStatement();
//        ) {
//            // You need to put all you queries here uaAmer
//            //for insertCategory
//            //2. prepare the query
//            String query = "SELECT * FROM CATEGORIES WHERE id = " + id + ";";
//            //3.execute the query
//            ResultSet result = statement.executeQuery(query);
//            //4. fetch result
//
//            if (result.next() == true) {
//                currentCategory.setId(result.getInt("id"));
//                currentCategory.setName(result.getString("name"));
//                currentCategory.setName(result.getString("description"));
//                return currentCategory;
//            } else {
//                return null;
//            }
//            //5.close connection
//            // Try with resource will close this connection uaAlaa
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public List<Categories> selectAll() {
//        List<Categories> categories = new ArrayList<Categories>();
//        try (
//                Connection connection = connectionManager.connect();
//                Statement statement = connection.createStatement();
//        ) {
//            // You need to put all you queries here uaAmer
//            //for insertCategory
//            //2. prepare the query
//            String query = "SELECT * FROM CATEGORIES ";
//            //3.execute the query
//            ResultSet result = statement.executeQuery(query);
//            //4. fetch result
//
//            while (result.next() == true) {
//                Categories currentCategory = new Categories(); // You need to make Object to fill fields
//                currentCategory.setId(result.getInt("id"));
//                currentCategory.setName(result.getString("name"));
//                currentCategory.setName(result.getString("description"));
//                categories.add(currentCategory);
//            }
//            //5.close connection
//            // Try with resource will close this connection uaAlaa
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return categories;
//    }
//
//    public void delete(int id) {
//        try (
//                Connection connection = connectionManager.connect();
//                Statement statement = connection.createStatement();
//        ) {
//            // You need to put all you queries here uaAmer
//            //for insertCategory
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//}


//        // This is used to make some updates to this row
//        try {
//            value = categoryRepo.updateCategory(category);
//        } catch (ClassNotFoundException | SQLException e) {
//            e.getMessage();
//        }
//        if (insertedRow) System.out.println("Row is inserted uaaemr ");
//        if (value) System.out.println("we have made some updates in the category table ");
//        else System.out.println("we cannot make changes to this cat");
//    }

//
//public boolean updateCategory(Categories category) throws ClassNotFoundException, SQLException {
//    boolean flag=false;
//    try (
//            Connection conn = ConnectionManager.getConnection();
//            Statement stmt = conn.createStatement();
//    ) {
//
//        String query = "UPDATE categories SET name = " +
//                "'" + category.getName() + "' , description = " +
//                "'" + category.getDescription() + "' " +
//                " WHERE id = " +
//                "'" + category.getId() + "' ;";
//        System.out.println(query);
//        int noAffectedRows = stmt.executeUpdate(query);
//        flag = noAffectedRows > 1;
//    }
//
//    catch (SQLException e) {
//        System.out.println(" WE cannot connect to database uaAmer ");
//    }
//    return flag;
//}
//
//}
