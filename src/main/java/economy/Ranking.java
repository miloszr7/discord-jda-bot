package economy;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Ranking extends ListenerAdapter {

    private final String url = "jdbc:sqlite:database.db";
    private final MoneyFormat moneyFormat = new MoneyFormat();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            if (event.getMessage().getContentRaw().equalsIgnoreCase("$ranking")) {

                Connection connection = DriverManager.getConnection(url);

                Statement st = connection.createStatement();

                String query = "SELECT * FROM bank ORDER BY Balance DESC";

                ResultSet resultSet = st.executeQuery(query);

                StringBuilder builder = new StringBuilder();

                int count = 0;

                while (resultSet.next()) {
                    count++;

                    builder.append(count).append(". ").append("**").append(resultSet.getString("Fullname")).append("**")
                            .append(" - $**").append(moneyFormat.comaFormat(resultSet.getLong("Balance"))).append("** \n");
                }

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.CYAN)
                                    .setDescription(builder)
                                        .build()
                ).queue();

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
