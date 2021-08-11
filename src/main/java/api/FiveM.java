package api;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;

public class FiveM extends ListenerAdapter {

    private final OkHttpClient client = new OkHttpClient();

    private final String URL = "https://servers-frontend.fivem.net/api/servers/single/";

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length == 2 && command[0].equalsIgnoreCase("$fivem")) {

                String server = command[1];

                URL website = new URL(URL + server);

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
                                .replaceAll("https://discord.gg/[A-Z]", "")
                                    .replace("[", "").replace("]", "") + "**";

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

                if (dataLength > 2047) {

                    if (isQueue(data)) {
                        event.getChannel().sendMessage(
                                new EmbedBuilder()
                                        .setThumbnail("https://i.imgur.com/UqW7rrg.png")
                                        .setDescription(hostname + "\n\n Currently `" + clients + "/" + maxClients +"` online.\n And theres " + queue + " in queue.")
                                        .setFooter("Theres currently too much data to show.\nDisplaying basic information.")
                                        .build()
                        ).queue();
                    } else {
                        event.getChannel().sendMessage(
                                new EmbedBuilder()
                                        .setThumbnail("https://i.imgur.com/UqW7rrg.png")
                                        .setDescription(hostname + "\n\n Currently `" + clients + "/" + maxClients +"` online.\n")
                                        .setFooter("Theres currently too much data to show.\nDisplaying basic information.")
                                        .build()
                        ).queue();
                    }

                } else {

                    if (isQueue(data)) {
                        event.getChannel().sendMessage(
                                new EmbedBuilder()
                                        .setThumbnail("https://i.imgur.com/UqW7rrg.png")
                                        .addField("ID", id.toString(), true)
                                        .addField("Name", name.toString(), true)
                                        .addField("", "Online **" + clients + "/" + maxClients
                                                + "**\nQueue **" + queue + "/" + queue + "**", true)
                                        .setDescription(hostname)
                                        .build()
                        ).queue();
                    } else {
                        event.getChannel().sendMessage(
                                new EmbedBuilder()
                                        .setThumbnail("https://i.imgur.com/UqW7rrg.png")
                                        .addField("ID", id.toString(), true)
                                        .addField("Name", name.toString(), true)
                                        .addField("", "Online **" + clients + "/" + maxClients + "**", true)
                                        .setDescription(hostname)
                                        .build()
                        ).queue();
                    }

                }

            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setThumbnail("https://i.imgur.com/UqW7rrg.png")
                            .setDescription("Something went wrong, please try again later.")
                    .build()
            ).queue();
        }

    }

    private boolean isQueue(JSONObject data) {
        return data.getString("hostname").startsWith("[");
    }
}
