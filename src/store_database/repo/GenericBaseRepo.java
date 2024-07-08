package store_database.repo;

import store_database.common.ConnectionManager;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenericBaseRepo {
    /*
     * --------- This would be the base Class -----------
     * // This will work for any class //
     * How to make generic insert
     * How to make generic update
     * how to make generic delete
     * How to make generic SelectAll
     * How to make generic select
     * // There are ready made classes for this but we will do our own class
     * */

    public static <T> boolean insert(T objectToInsert) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean isInserted = false;

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            conn = ConnectionManager.getConnection();

            // Prepare SQL insert query
            String tableName = objectToInsert.getClass().getSimpleName().toLowerCase();
            String sql = generateInsertQuery(tableName, objectToInsert);   // This is second part difficult

            // Create PreparedStatement
            pstmt = conn.prepareStatement(sql);

            // Set parameters based on object fields
            setParameters(pstmt, objectToInsert);            // This is the third part difficult

            // Execute insert
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            // Check if the insert was successful
            isInserted = rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
            throw new SQLException("Failed to insert object: " + e.getMessage());
        } finally {
            // Close resources
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return isInserted;
    }

    // Generate insert query dynamically using object fields
    // الفكرة كلها انك هتفصص الاستعلام اللي هيكتب بشكل dynamic
    private static <T> String generateInsertQuery(String tableName, T objectToInsert) throws IllegalAccessException {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName).append(" (");

        // Get object class fields
        Field[] fields = objectToInsert.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true); // Enable access to private fields
            String fieldName = fields[i].getName();
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(fieldName);
        }

        // Append values keyword
        sql.append(") VALUES (");

        // Append placeholders for values
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append("?");
        }

        sql.append(")");

        return sql.toString();
    }


    // used in the prepared Statement Object that you are making to insert queries to database instead of normal query
    // prepared statement is used to enhance security of you database
    private static <T> void setParameters(PreparedStatement pstmt, T objectToInsert) throws SQLException, IllegalAccessException {
        Field[] fields = objectToInsert.getClass().getDeclaredFields();
        int parameterIndex = 1;
        for (Field field : fields) {
            field.setAccessible(true); // Enable access to private fields

            Object value = field.get(objectToInsert);
            pstmt.setObject(parameterIndex++, value);
        }
    }


    //---------------------------------------------------------------------------------
    public static <T> boolean delete(T objectToDelete, String tableName) throws IllegalAccessException, SQLException {
        /*
         * 1- we need to make connection to database
         * 3- you need to make the query to give it to prepared Statement
         * 3- create prepared statement
         * 4- setParameters
         * */
        Class<?> clazz = objectToDelete.getClass();
        String clazzName = clazz.getSimpleName();
        Field[] fields = clazz.getDeclaredFields();

        Field idField = null;

        for (Field field : fields) {
            if (field.getName().equals("id")) {
                idField = field;
                field.setAccessible(true);
                break;
            }
        }
        if (idField == null) {
            throw new IllegalAccessException();
        }
        // second step you need to prepare the query
        String query = "DELETE FROM " + tableName + "WHERE  id = ? ";


        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
        ) {
            Object idValue = idField.get(objectToDelete);
            pstmt.setObject(1, idValue);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected != 0) {
                System.out.println("Number of affected Rows : " + rowsAffected);
                return true;
            } else return false;
        }
        // you need to make you prepared Statement uaamer
    }
}