import java.sql.*;

public class DatabaseConnection {

    private Connection connection ;

    public DatabaseConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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

        Statement query = connection.createStatement();

        query.executeUpdate(SQL);

    }

    public void openConnection() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
    }

    public ResultSet resultSet(String query) throws Exception {

        Statement st = connection.createStatement();

        return st.executeQuery(query);

    }

    public Connection getConnection() {
        return connection;
    }
}
