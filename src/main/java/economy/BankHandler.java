package economy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BankHandler {

    public boolean verify(int amount, long AccountNumber) throws Exception {

        String url = "jdbc:sqlite:database.db";
        Connection connection = DriverManager.getConnection(url);

        Statement st = connection.createStatement();

        String check = "SELECT Balance FROM bank WHERE AccountNumber = " + AccountNumber + "";

        ResultSet resultSet = st.executeQuery(check);

        int CurrentBalance = 0;

        while (resultSet.next()) {
            CurrentBalance = resultSet.getInt("Balance");
        }

        if (CurrentBalance >= amount) {

            String remove = "SELECT sum(Balance) - " + amount + " FROM bank WHERE AccountNumber = " + AccountNumber + " "; // Remove money

            String update = "UPDATE bank SET Balance = Balance - " + amount + " WHERE AccountNumber = " + AccountNumber + " "; // Update balance

            st.executeUpdate(remove);
            st.executeUpdate(update);

            connection.close();

            return true;

        }

        return false;

    }

    public void addMoney(double amount, long AccountNumber) throws Exception {

        String url = "jdbc:sqlite:database.db";
        Connection connection = DriverManager.getConnection(url);

        Statement st = connection.createStatement();

        String check = "SELECT Balance FROM bank WHERE AccountNumber = " + AccountNumber + " ";

        ResultSet resultSet = st.executeQuery(check);

        String CurrentBalance = "";

        while (resultSet.next()) {
            CurrentBalance = resultSet.getString("Balance");
        }

        String update = "UPDATE bank SET Balance = Balance + " + amount + " WHERE AccountNumber = " + AccountNumber + " ";

        st.executeUpdate(update);

        connection.close();

    }

    public void addMoney(int amount, long AccountNumber) throws Exception {

        String url = "jdbc:sqlite:database.db";
        Connection connection = DriverManager.getConnection(url);

        Statement st = connection.createStatement();

        String check = "SELECT Balance FROM bank WHERE AccountNumber = " + AccountNumber + " ";

        ResultSet resultSet = st.executeQuery(check);

        String CurrentBalance = "";

        while (resultSet.next()) {
            CurrentBalance = resultSet.getString("Balance");
        }

        String update = "UPDATE bank SET Balance = Balance + " + amount + " WHERE AccountNumber = " + AccountNumber + " ";

        st.executeUpdate(update);

        connection.close();

    }


}
