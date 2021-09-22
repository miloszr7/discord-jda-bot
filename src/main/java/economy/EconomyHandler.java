package economy;

import config.DatabaseConnection;

import java.sql.ResultSet;

public class EconomyHandler {

    private DatabaseConnection connection = null;

    public boolean verify(int amount, long AccountNumber) throws Exception {

        int balance = getBalance(AccountNumber);

        if (balance >= amount) {

            connection.query("UPDATE bank SET Balance = Balance - " + amount + " WHERE AccountNumber = " + AccountNumber + " ");
            connection.getConnection().close();

            return true;
        }

        return false;

    }

    private int getBalance(long accountNumber) throws Exception {
        connection = new DatabaseConnection();

        ResultSet checkBalance = connection.resultSet("SELECT Balance FROM bank WHERE AccountNumber = "+ accountNumber +"");

        int balance = 0;

        while (checkBalance.next()) {
            balance = checkBalance.getInt("Balance");
        }

        checkBalance.close();

        return balance;
    }

    public void addMoney(double amount, long accountNumber) throws Exception {

        connection = new DatabaseConnection();

        connection.query("UPDATE bank SET Balance = Balance + "+amount+" WHERE AccountNumber = "+accountNumber+"");

        connection.getConnection().close();

    }


}
