package slash;

import config.Tokens;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;

public class YouTube extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        if (event.getName().equals("youtube")) {

            String search = event.getOption("search").getAsString();

            try {

                event.getInteraction().reply(youtubeSearch(search, 15)).queue();

            } catch (Exception ex) {

                if (ex.getMessage().equalsIgnoreCase("JSONObject[\"videoId\"] not found.")) {
                    event.getInteraction().reply("Could not find a youtube video with provided search query.").queue();
                }

                System.out.println("[Youtube API] :: " + ex.getMessage());
            }

        }

    }

    private String youtubeSearch(String keyword, int maxResults) throws Exception {

        OkHttpClient client = new OkHttpClient();

        // Returns random video <= maxResults
        int videoIndex = (int) (Math.random() * maxResults);

        String URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&regionCode=US&maxResults=" + maxResults +
                "&q=" + keyword.replaceAll(" ", "_") + "&key=" + Tokens.YOUTUBE_API_KEY;

        URL website = new URL(URL);

        Request request = new Request.Builder()
                .url(website)
                .get()
                .build();

        String content = "";

        JSONObject json;

        try (Response response = client.newCall(request).execute()) {

            content = Objects.requireNonNull(response.body()).string();

            json = new JSONObject(content);

            response.body().close();

        }

        JSONArray data = json.getJSONArray("items");

        String videoId = data.getJSONObject(videoIndex).getJSONObject("id").getString("videoId");

        String youtubeLink = "https://www.youtube.com/watch?v=";

        String finalLink = null;

        if (!videoId.startsWith("{\"\":\"youtube#channel\",") || videoId.contains("playlistId")) {
            finalLink = youtubeLink + videoId;
        }

        return finalLink;

    }

}
