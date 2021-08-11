package economy;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AccountCreation extends ListenerAdapter {

    private final String url = "jdbc:sqlite:database.db";
    private ArrayList<String> users = new ArrayList<>();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            if (!users.contains(event.getAuthor().getId())) {

                Connection connection = DriverManager.getConnection(url);

                Statement st = connection.createStatement();

                String query = "SELECT AccountNumber FROM bank WHERE AccountNumber = " + event.getAuthor().getIdLong() + " ";

                ResultSet resultSet = st.executeQuery(query);

                long check_acc_number = 0;

                while (resultSet.next()) {
                    check_acc_number = resultSet.getLong("AccountNumber");
                }

                users.add(event.getAuthor().getId());

                connection.close();

                if (check_acc_number != event.getAuthor().getIdLong()) {

                    Connection conn = DriverManager.getConnection(url);

                    String username = event.getAuthor().getName();
                    long accountNumber = event.getAuthor().getIdLong();
                    long bankID = event.getGuild().getIdLong();

                    Statement statement = conn.createStatement();

                    String create_account = "INSERT INTO bank VALUES (null, '" + username + "', '" + accountNumber + "', '" + bankID + "', 1000)";

                    statement.executeUpdate(create_account);

                    Statement statement_2 = conn.createStatement();

                    String createInventory = "INSERT INTO inventory VALUES (null, " + event.getAuthor().getIdLong() + ", '" + event.getAuthor().getAsTag() + "', null)";

                    statement_2.executeUpdate(createInventory);

                    users.add(event.getAuthor().getId());

                    conn.close();

                }

                if (users.size() == 1000) {
                    users.clear();
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
