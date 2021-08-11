package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    public static void connect() {
        Connection connection = null;

        try {

            String url = "jdbc:sqlite:database.db";

            connection = DriverManager.getConnection(url);

            System.out.println("Connected to database.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void query(String SQL) throws Exception {

        String URL = "jdbc:sqlite:database.db";

        Connection connection = DriverManager.getConnection(URL);

        Statement query = connection.createStatement();

        query.executeUpdate(SQL);

        connection.close();

    }

}
