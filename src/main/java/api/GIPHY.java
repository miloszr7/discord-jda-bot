package api;

import config.Tokens;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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

public class GIPHY extends ListenerAdapter {

    private final OkHttpClient client = new OkHttpClient();

    private String query;
    private int offset;

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length != 1 && command[0].equalsIgnoreCase("$gif")) {

            StringBuilder q = new StringBuilder();

            for (int i = 1; i < command.length; i++) {
                q.append(command[i]).append(" ");
            }

            setQuery(q.toString());

            int offset = (int) (Math.random() * 50);

            setOffset(offset);

            String GIPHY_URL = "https://api.giphy.com/v1/gifs/search?api_key="+ Tokens.GIPHY_API_KEY+"" +
                    "&q="+ getQuery() +"&limit=50&offset="+ getOffset() +"&rating=g&lang=en";

            try {

                URL website = new URL(GIPHY_URL);

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

                JSONArray arr = json.getJSONArray("data");

                String title = arr.getJSONObject(0).getString("title");

                String url = json.getJSONArray("data")
                                .getJSONObject(0)
                                    .getJSONObject("images")
                                        .getJSONObject("original")
                                            .getString("url");

                EmbedBuilder message = new EmbedBuilder()
                        .setTitle("**`" + title + "`**")
                            .setColor(Color.CYAN)
                                .setImage(url);

                event.getChannel().sendMessage(message.build()).queue();

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }

    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}
