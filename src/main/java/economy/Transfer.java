package economy;

import config.DatabaseConnection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.ResultSet;

public class Transfer extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length == 3 && command[0].equalsIgnoreCase("$pay")) {

            Member mentioned = event.getMessage().getMentionedMembers().get(0);

            long targetNumber = mentioned.getIdLong();
            long accountNumber = event.getAuthor().getIdLong();

            try {

                int amount = Integer.parseInt(command[2]);

                DatabaseConnection connection = new DatabaseConnection();

                ResultSet selectBalance = connection.resultSet("SELECT Balance FROM bank WHERE AccountNumber = "+ accountNumber +"");

                int userBalance = 0;

                while (selectBalance.next()) {
                    userBalance = selectBalance.getInt("Balance");
                }

                selectBalance.close();

                if (userBalance >= amount) {

                    String transaction = "BEGIN TRANSACTION;" +
                            "UPDATE bank SET Balance = Balance - "+amount+" WHERE AccountNumber = "+accountNumber+";" +
                            "UPDATE bank SET Balance = Balance + "+amount+" WHERE AccountNumber = "+targetNumber+";" +
                            "COMMIT;";

                    connection.query(transaction);
                    connection.getConnection().close();

                    event.getChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.decode("#4153FF"))
                                    .setDescription(":receipt: You have transferred **$" + MoneyFormat.comaFormat(amount) + "** to **" + mentioned.getUser().getAsTag() + "**")
                                    .build()
                    ).queue();

                } else if (userBalance < amount) {

                    event.getChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.decode("#4153FF"))
                                    .setDescription("You don't have enough money to make a transfer.")
                                    .build()
                    ).queue();

                }

            } catch (Exception e) {
                System.out.println("[Bank Transfer] :: " + e.getMessage());
            }

        }

    }
}
