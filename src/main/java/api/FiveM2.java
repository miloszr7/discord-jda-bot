package api;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FiveM2 extends ListenerAdapter {

    private final OkHttpClient client = new OkHttpClient();

    private final String URL = "https://servers-frontend.fivem.net/api/servers/single/";

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length == 2 && command[0].equalsIgnoreCase(".status")) {

                EmbedBuilder embedMessage = new EmbedBuilder();

                embedMessage.setTitle("Live Status");
                embedMessage.setThumbnail("https://i.imgur.com/UqW7rrg.png");

                event.getChannel().sendMessage(embedMessage.build()).queue(e -> {

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            String server = command[1];

                            try {

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

                                if (isQueue(data)) {

                                    embedMessage.setDescription(hostname + "\n\n Currently `" + clients + "/" + maxClients +"` online.\n And theres " + queue + " in queue.");
                                    embedMessage.setColor(Color.decode("#F40552"));

                                    e.editMessage(embedMessage.build()).queue();

                                    embedMessage.setColor(Color.BLACK);

                                    e.editMessage(embedMessage.build()).queue();

                                } else {

                                    embedMessage.setDescription(hostname + "\n\n Currently `" + clients + "/" + maxClients +"` online.\n");
                                    embedMessage.setColor(Color.decode("#F40552"));

                                    e.editMessage(embedMessage.build()).queue();

                                    embedMessage.setColor(Color.BLACK);

                                    e.editMessage(embedMessage.build()).queue();

                                }


                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }

                        }
                    };

                    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

                    // Execute function in specified period of time.
                    // Period is defined in seconds but it can be change at any time.

                    executor.scheduleAtFixedRate(runnable,0,3, TimeUnit.SECONDS);

                });

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
