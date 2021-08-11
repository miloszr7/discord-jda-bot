package commands;

import config.DatabaseConnection;
import config.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Invitation extends ListenerAdapter {

    private final Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (!event.getAuthor().isBot() && !originalServerInvitation(event, event.getMessage().getContentRaw())) {
            detectInvitation(event, event.getMessage().getContentRaw());
        }

        //

        if (event.getMessage().getContentRaw().equalsIgnoreCase("$invites")
                && permissions.ADMINISTRATOR(event, event.getMember()) && !isEnabled(event.getGuild().getId())) {

            DatabaseConnection connection = new DatabaseConnection();

            try {

                connection.query("INSERT INTO invites VALUES ('"+event.getGuild().getId()+"')");

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.CYAN)
                                .setDescription("You have enabled protection from invites.\n" +
                                        "Protected permissions: **Administrator** & **Ban Members**")
                        .build()
                ).queue();

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }

        } else if (event.getMessage().getContentRaw().equalsIgnoreCase("$invites")
                && permissions.ADMINISTRATOR(event, event.getMember()) && isEnabled(event.getGuild().getId())) {

            event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setDescription("You have already enabled protection from invites.")
                            .setFooter("Use $inv remove to disable this feature.")
                            .build()
            ).queue();

        }

        if (event.getMessage().getContentRaw().equalsIgnoreCase("$inv remove")
                && permissions.ADMINISTRATOR(event, event.getMember())) {

            DatabaseConnection connection = new DatabaseConnection();

            try {

                connection.query("DELETE FROM invites WHERE ID = '"+event.getGuild().getId()+"'");

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.CYAN)
                                .setDescription("You have removed protection from invites.")
                                .build()
                ).queue();

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }

        }

    }

    private boolean isEnabled(String serverID) {

        try {

            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");

            Statement st = connection.createStatement();

            String query = "SELECT * FROM invites WHERE ID = " + serverID + " ";

            ResultSet resultSet = st.executeQuery(query);

            while (resultSet.next()) {
                if (resultSet.getString("ID").equals(serverID)) {
                    return true;
                }
            }

            connection.close();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return false;
    }

    private boolean originalServerInvitation(GuildMessageReceivedEvent event, String message) {

        List<Invite> inviteList = event.getChannel().retrieveInvites().complete();

        String invCode = message.replace("https://discord.gg/", "");

        for (Invite invite : inviteList) {
            if (invCode.equals(invite.getCode())) {
                return true;
            }
        }

        return false;

    }

    private void detectInvitation(GuildMessageReceivedEvent event, String message) {

        //if (!permissions.ADMINISTRATOR(event.getMember()) || !permissions.BAN_MEMBERS(event.getMember())) {

            String regex1 = "(https?://)?(www.)?(discord.(gg|io|me|li)|discordapp.com/invite)/[^\\s/]+?(?=\\b)";
            //String regex2 = "/https(:)\\/\\/discord.gg\\/[a-zA-Z0-9]+/g";
            //String regex3 = "https(:)\\/\\/ discord.gg\\/[a-zA-Z0-9]+";

            String invitationCodeRegex = "[a-zA-Z0-9\\-_]{7,10}";

            // Find only inv code hidden inside the message
            // example: hey guys join this server here is inv code: mNpXDZwYaZ
            Pattern codePattern = Pattern.compile(invitationCodeRegex);
            Matcher codeMatcher = codePattern.matcher(message);

            boolean containsCode = codeMatcher.find();

            //

            // Find discord invite link inside the message
            // example: join this server https://discord.gg/XXXXX
            Pattern messageInvPattern = Pattern.compile(regex1);
            Matcher messageInvMatcher = messageInvPattern.matcher(message);

            boolean containsInvLink = messageInvMatcher.find();

            if (containsInvLink) {
                removeMessage(event);
            }

            /*
                        if (containsCode && invitationLength(codeMatcher.group())) {

                try {

                    if (!codeMatcher.group().startsWith("haha") && validateInvitationCode(codeMatcher.group())) {
                        removeMessage(event);
                    }

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

            }
             */

        //}

    }

    private void removeMessage(GuildMessageReceivedEvent event) {

        event.getMessage().delete().queue();

        event.getChannel().sendMessage(
                new EmbedBuilder()
                        .setAuthor(event.getAuthor().getAsTag() + " posted an invite", event.getAuthor().getAvatarUrl(), event.getAuthor().getAvatarUrl())
                        .build()
        ).queue();

    }

    private boolean validateInvitationCode(String invCode) throws Exception {

        OkHttpClient client = new OkHttpClient();

        URL website = new URL("https://discordapp.com/api/v6/invite/" + invCode);

        Request request = new Request.Builder()
                .url(website)
                .get()
                .build();

        String content = "";

        JSONObject json;

        try (Response response = client.newCall(request).execute()) {

            content = Objects.requireNonNull(response.body()).string();

            json = new JSONObject(content);

        }

        int length = json.toString().length();

        // 41 = not invitation code
        // > 100 - inv code

        if (length <= 41) {
            return false;
        } else return length > 100;
    }

    private boolean invitationLength(String code) {

        return code.length() >= 7 && code.length() < 11;
    }

}
