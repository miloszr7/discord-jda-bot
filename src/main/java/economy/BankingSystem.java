package economy;

import commands.CleanMessages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BankingSystem extends ListenerAdapter {

    public boolean check(String ID) {
        return ID.equals("588420813674512404");
    }

    private final String url = "jdbc:sqlite:database.db";

    public MoneyFormat moneyFormat = new MoneyFormat();

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        try {

            String[] message = event.getMessage().getContentRaw().split(" ");

            if (message.length == 2 && message[0].equalsIgnoreCase("$profile")) {

                Connection connection = DriverManager.getConnection(url);

                Statement st = connection.createStatement();

                Member member = event.getMessage().getMentionedMembers().get(0);

                String avatar = member.getUser().getAvatarUrl();
                String userID = member.getUser().getId();
                String joined = member.getTimeJoined().format(DateTimeFormatter.ofPattern("MM/dd/YYYY"));
                String status = member.getOnlineStatus().toString();
                String created = member.getTimeCreated().format(DateTimeFormatter.ofPattern("MM/dd/YYYY"));

                EmbedBuilder embed = new EmbedBuilder();

                String SQL = "SELECT Balance FROM bank WHERE AccountNumber = " + member.getIdLong() + " ";

                ResultSet result = st.executeQuery(SQL);

                long balance = 0;

                while (result.next()) {
                    balance = result.getLong("Balance");
                }

                List<Role> roles = member.getRoles();

                if (roles.isEmpty()) {

                    embed.setDescription(
                                    "**Status**: " + status + "\n" +
                                    "**ID**: " + userID + "\n" +
                                    "**Joined**: " + joined + "\n" +
                                    "**Account created**: " + created + "\n" +
                                    "**Balance**: $" + moneyFormat.comaFormat(balance) + "\n"
                    );

                } else {

                    embed.setDescription(
                            roles.get(0).getAsMention() + "\n\n" +
                                    "**ID**: " + userID + "\n" +
                                    "**Status**: " + status + "\n" +
                                    "**Joined**: " + joined + "\n" +
                                    "**Account created**: " + created + "\n" +
                                    "**Balance**: $" + moneyFormat.comaFormat(balance) + "\n"
                    );

                }

                embed.setTitle("**" + member.getUser().getAsTag() + "**");
                embed.setColor(Color.decode(CleanMessages.EmbedColor));
                embed.setThumbnail(avatar);

                event.getChannel().sendMessage(embed.build()).queue();

                connection.close();

            } else if (event.getMessage().getContentRaw().equalsIgnoreCase("$profile")) {

                Connection connection = DriverManager.getConnection(url);

                Statement st = connection.createStatement();

                String avatar = event.getAuthor().getAvatarUrl();
                String userID = event.getAuthor().getId();
                String joined = event.getMember().getTimeJoined().format(DateTimeFormatter.ofPattern("MM/dd/YYYY"));
                String status = event.getMember().getOnlineStatus().toString();
                String created = event.getAuthor().getTimeCreated().format(DateTimeFormatter.ofPattern("MM/dd/YYYY"));

                EmbedBuilder embed = new EmbedBuilder();

                String SQL = "SELECT Balance FROM bank WHERE AccountNumber = " + event.getAuthor().getIdLong() + " ";

                ResultSet result = st.executeQuery(SQL);

                long balance = 0;

                while (result.next()) {
                    balance = result.getLong("Balance");
                }

                List<Role> roles = event.getMember().getRoles();

                if (roles.isEmpty()) {

                    embed.setDescription(
                            "**Status**: " + status + "\n" +
                                    "**ID**: " + userID + "\n" +
                                    "**Joined**: " + joined + "\n" +
                                    "**Account created**: " + created + "\n" +
                                    "**Balance**: $" + moneyFormat.comaFormat(balance) + "\n"
                    );

                } else {

                    embed.setDescription(
                            roles.get(0).getAsMention() + "\n\n" +
                                    "**ID**: " + userID + "\n" +
                                    "**Status**: " + status + "\n" +
                                    "**Joined**: " + joined + "\n" +
                                    "**Account created**: " + created + "\n" +
                                    "**Balance**: $" + moneyFormat.comaFormat(balance) + "\n"
                    );

                }

                embed.setTitle("**" + event.getMember().getUser().getAsTag() + "**");
                embed.setColor(Color.decode(CleanMessages.EmbedColor));
                embed.setThumbnail(avatar);

                event.getChannel().sendMessage(embed.build()).queue();

                connection.close();

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
