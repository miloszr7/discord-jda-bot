package slash;

import exception.GlobalExceptionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class FiveM extends ListenerAdapter {

    private final OkHttpClient client = new OkHttpClient();

    private final String URL = "https://servers-frontend.fivem.net/api/servers/single/";

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        if (event.getName().equals("fivem")) {

            String endpoint = event.getOption("endpoint").getAsString();

            try {

                showFiveMServerInfo(event, endpoint);

            } catch (Exception ex) {

                GlobalExceptionHandler.handleException(event, "FiveM", ex);

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setDescription("Something went wrong, please try again later.")
                                .build()
                ).queue();

            }

        }

    }

    private void showFiveMServerInfo(SlashCommandEvent event, String endpoint) throws Exception {

        URL website = new URL(URL + endpoint);

        Request request = new Request.Builder()
                .url(website)
                .get()
                .build();

        String content = "";

        JSONObject json ;

        try (Response response = client.newCall(request).execute()) {

            content = Objects.requireNonNull(response.body()).string();

            json = new JSONObject(content);

        }

        JSONObject data = json.getJSONObject("Data");

        String hostname = "**" + data.getString("hostname")
                .replaceAll("[0-9]","")
                .replaceAll("\\^", "")
                .replaceAll("(https?://)?(www.)?(discord.(gg|io|me|li)|discordapp.com/invite)/[^\\s/]+?(?=\\b)", "") + "**";

        String queue = data.getString("hostname").substring(0, 3)
                .replace("[", "").replace("]", "");

        int clients = data.getInt("clients");
        int maxClients = data.getInt("sv_maxclients");
        int online = data.getJSONArray("players").length();

        JSONArray players = data.getJSONArray("players");

        StringBuilder name = new StringBuilder();
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < online; i++) {
            id.append(players.getJSONObject(i).getInt("id")).append("\n");
            name.append(players.getJSONObject(i).getString("name")).append("\n");
        }

        int dataLength = name.length() + id.length() + hostname.length();

        Color embedColor = Color.decode("#FF0064");

        if (dataLength > 2047) {

            if (isQueue(data)) {
                event.getInteraction().replyEmbeds(
                        new EmbedBuilder()
                                .setThumbnail("https://i.imgur.com/UqW7rrg.png")
                                .setDescription(hostname + "\n\n Currently `" + clients + "/" + maxClients +"` online.\n And theres " + queue + " in queue.")
                                .setFooter("There is too much data to show.\nDisplaying basic information.")
                                .build()
                ).queue();

            } else {

                event.getInteraction().replyEmbeds(
                        new EmbedBuilder()
                                .setThumbnail("https://i.imgur.com/UqW7rrg.png")
                                .setDescription(hostname + "\n\n Currently `" + clients + "/" + maxClients +"` online.\n")
                                .setFooter("There is too much data to show.\nDisplaying basic information.")
                                .build()
                ).queue();

            }

        } else {

            if (isQueue(data)) {

                event.getInteraction().replyEmbeds(
                        new EmbedBuilder()
                                .setThumbnail("https://i.imgur.com/UqW7rrg.png")
                                .addField("ID", id.toString(), true)
                                .addField("Steam username", name.toString().replace("*", ""), true)
                                .addField("", "Online **" + clients + "/" + maxClients
                                        + "**\nQueue **" + queue + "/" + queue + "**", true)
                                .setDescription(hostname)
                                .build()
                ).queue();

            } else {

                event.getInteraction().replyEmbeds(
                        new EmbedBuilder()
                                .setThumbnail("https://i.imgur.com/UqW7rrg.png")
                                .addField("ID", id.toString(), true)
                                .addField("Steam username", name.toString().replace("*", ""), true)
                                .addField("", "Online **" + clients + "/" + maxClients + "**", true)
                                .setDescription(hostname)
                                .build()
                ).queue();

            }

        }

    }

    private boolean isQueue(JSONObject data) {
        return data.getString("hostname").startsWith("[");
    }
}
