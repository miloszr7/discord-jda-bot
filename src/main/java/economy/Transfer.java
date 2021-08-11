package economy;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class Transfer extends ListenerAdapter {

    public BankingSystem bankingSystem = new BankingSystem();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length == 3 && command[0].equalsIgnoreCase("$pay")) {
            event.getMessage().delete().queue();

            Member mentioned = event.getMessage().getMentionedMembers().get(0);

            long TargetNumber = mentioned.getIdLong();

            long AccountNumber = event.getAuthor().getIdLong();

            int amount = Integer.parseInt(command[2]);

            try {

                String url = "jdbc:sqlite:database.db";
                Connection connection = DriverManager.getConnection(url);

                Statement st = connection.createStatement();

                String check = "SELECT Balance FROM bank WHERE AccountNumber = " + AccountNumber + " ";

                ResultSet resultSet = st.executeQuery(check);

                String CurrentBalance = "";

                while (resultSet.next()) {
                    CurrentBalance = resultSet.getString("Balance");
                }

                if (Integer.parseInt(CurrentBalance) >= amount) {

                    String sql = "SELECT sum(Balance) - " + amount + " FROM bank WHERE AccountNumber = " + AccountNumber + " "; // Remove money

                    String sql2 = "UPDATE bank SET Balance = Balance - " + amount + " WHERE AccountNumber = " + AccountNumber + " "; // Update balance

                    String sql3 = "UPDATE bank SET Balance = Balance + " + amount + " WHERE AccountNumber = " + TargetNumber + " "; // Transfer to someone

                    st.executeUpdate(sql);
                    st.executeUpdate(sql2);
                    st.executeUpdate(sql3);

                    event.getChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.decode("#4153FF"))
                                    .setDescription(":receipt: " + event.getAuthor().getAsMention() + " you've transferred $**" + amount + "** to " + mentioned.getAsMention())
                                    .build()
                    ).queue();

                    connection.close();

                } else if (Integer.parseInt(CurrentBalance) < amount) {

                    event.getChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.decode("#4153FF"))
                                    .setDescription(":name_badge: " + event.getAuthor().getAsMention() + " you don't have enough money to make transfer.")
                                    .build()
                    ).complete().delete().queueAfter(5, TimeUnit.SECONDS);

                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }
}
