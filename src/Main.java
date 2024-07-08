import store_database.entity.Brands;
import store_database.entity.Categories;
import store_database.entity.Cities;
import store_database.repo.GenericBaseRepo;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/store_java";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {


        Categories categories = new Categories();
        categories.setId(12);
        categories.setName("Third generic Category");
        categories.setDescription("This is the Third Generic Object ");


        // How to use generic Class in your code
        // Just single and one method used to insert any Object to its table uaAmer
        // This is so great mission
        // This is just a generic function uaAmer
        try {
            GenericBaseRepo.insert(categories);
//            GenericBaseRepo.insert(brands);
//            GenericBaseRepo.insert(cities);
        } catch (SQLException e) {
            System.out.println("We cannot connecto the database ");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception");
        }
    }

//    public static Connection getConnection() {
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            System.out.println("Driver error check it again ");
//        }
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(URL, USER, PASSWORD);
//        } catch (SQLException e) {
//            System.out.println("Error connection");
//        }
//
//        if (conn == null) System.out.println("there is an error in the connetion");
//        else System.out.println("This is so right ");
//
//        return conn;
//    }

}