package economy;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AddMoney extends ListenerAdapter {

    public BankingSystem bankingSystem = new BankingSystem();
    public MoneyFormat moneyFormat = new MoneyFormat();

    private final String url = "jdbc:sqlite:database.db";

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        try {

            if (command.length == 3 && command[0].equalsIgnoreCase("*money")
                    && bankingSystem.check(event.getAuthor().getId())) {

                long targetNumber = Long.parseLong(command[1]);

                long balance = Long.parseLong(command[2]);

                Connection connection = DriverManager.getConnection(url);

                Statement st = connection.createStatement();

                String sql = "UPDATE bank SET Balance = Balance + " + balance + " WHERE AccountNumber = " + targetNumber + " ";

                st.executeUpdate(sql);

                connection.close();

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.decode("#4153FF"))
                                    .setDescription(":receipt: You've added **$" + moneyFormat.comaFormat(balance) + "** for `" + targetNumber + "`")
                                        .build()

                ).queue();

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
