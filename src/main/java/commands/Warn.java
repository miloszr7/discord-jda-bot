package commands;

import config.DatabaseConnection;
import config.Permissions;
import ratelimit.Cooldown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class Warn extends ListenerAdapter {

    private final Permissions permissions = new Permissions();
    private final Cooldown cooldown = new Cooldown();

    private final String url = "jdbc:sqlite:database.db";

    private Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        // have to remove previous message with warn history to avoid overwriting member by another message call
        // its caused by discord buttons and its separate method
        try {
            //removePreviousWarnHistory(event);
        } catch (NullPointerException exception) {
            System.out.println(exception.getMessage());
        }

        try {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command[0].equalsIgnoreCase("$warn")
                    && permissions.KICK_MEMBERS(event, event.getMember())
                        && !cooldown.hasCooldown(event.getMember())) {

                cooldown.activate(event, "$warn");

                Member member = event.getMessage().getMentionedMembers().get(0);

                StringBuilder description = new StringBuilder();

                for (int i = 2; i < command.length; i++) {
                    description.append(command[i]).append(" ");
                }

                String issued = event.getMessage().getTimeCreated().
                        format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

                DatabaseConnection connection = new DatabaseConnection();

                connection.query("INSERT INTO warns VALUES(" +
                        "null,'"+member.getId()+"', '"+member.getUser().getAsTag()+"', '"+description+"', '"+issued+"')");

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setTitle(member.getUser().getAsTag() + " has been warned")
                                .setDescription("Reason: " + description)
                        .build()
                ).queue();

            }

            //

            // check if bot has permission to remove messages, otherwise removePreviousWarnHistory() wont work

                if (command[0].equalsIgnoreCase("$warns")
                        && command.length == 2 && permissions.KICK_MEMBERS(event, event.getMember())
                        && !cooldown.hasCooldown(event.getMember())) {

                    cooldown.activate(event, "$warns");

                    Member member = event.getMessage().getMentionedMembers().get(0);

                    Connection connection = DriverManager.getConnection(url);

                    Statement st = connection.createStatement();

                    String query = "SELECT * FROM warns WHERE USER_ID = " + member.getId() + " ";

                    ResultSet resultSet = st.executeQuery(query);

                    StringBuilder username = new StringBuilder();
                    StringBuilder reason = new StringBuilder();
                    StringBuilder time = new StringBuilder();

                    int count = 0;

                    while (resultSet.next()) {

                        username.append(resultSet.getString("USERNAME")).append("\n");
                        reason.append(resultSet.getString("REASON")).append("\n");
                        time.append(resultSet.getString("WHEN")).append("\n");

                        count++;
                    }

                    if (count == 0) {
                        event.getChannel().sendMessage(member.getUser().getAsTag() + " has currently no infractions.").queue();
                    } else {

                        Button clear = Button.danger("warns_clear", "Clear an infractions");
                        Button kick = Button.primary("warns_kick", "Issue a kick");
                        Button ban = Button.primary("warns_ban", "Issue a ban");

                        event.getChannel().sendMessage(
                                new EmbedBuilder()
                                        .setTitle("Warn History")
                                        .addField("Member", username.toString(), true)
                                        .addField("Reason", reason.toString(), true)
                                        .addField("When", time.toString(), true)
                                        .setFooter("Currently " + count + " warns.")
                                        .build()
                        ).setActionRow(
                                clear, kick, ban
                        ).queue();

                        setMember(member);

                    }

                }



        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {

        if (event.getComponentId().equals("warns_clear")
                && permissions.KICK_MEMBERS(event.getMember())) {

            if (isMemberSpecified(event, member)) {

                removeInfractions();

                // reset
                setMember(null);

                event.getInteraction().deferEdit().queue();

            }

        }

        if (event.getComponentId().equals("warns_kick")
                && permissions.KICK_MEMBERS(event.getMember())) {

            if (isMemberSpecified(event, member)) {

                event.getGuild().kick(getMember()).queue();

                removeInfractions();

                // reset
                setMember(null);

            }

            event.getInteraction().deferEdit().queue();

        }

        if (event.getComponentId().equals("warns_ban")
                && permissions.KICK_MEMBERS(event.getMember())) {

            if (isMemberSpecified(event, member)) {

                event.getGuild().ban(getMember(), 7).queue();

                removeInfractions();

                // reset
                setMember(null);

            }

            event.getInteraction().deferEdit().queue();

        }

    }

    private void removePreviousWarnHistory(@NotNull GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {

            List<MessageEmbed> messageEmbedList = event.getMessage().getEmbeds();

            for (MessageEmbed messageEmbed : messageEmbedList) {

                if (messageEmbed.getTitle().equals("Warn History")) {

                    try {

                        MessageHistory history = event.getChannel().getHistory();

                        event.getChannel().deleteMessages(history.retrievePast(2).complete()).queue();

                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }

                }

            }

        }
    }

    private boolean isMemberSpecified(@NotNull ButtonClickEvent event, Member member) {

        if (member == null) {
            event.getChannel().sendMessage("You have already used interaction. In order to refresh member use this command again.").queue();
            return false;
        }

        return true;

    }
    private void removeInfractions() {
        try {
            new DatabaseConnection().query("DELETE FROM warns WHERE USER_ID = '"+getMember().getId()+"'");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
