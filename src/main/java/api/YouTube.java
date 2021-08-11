package api;

import config.Tokens;
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
import java.util.concurrent.atomic.AtomicInteger;

public class YouTube extends ListenerAdapter {

    private final OkHttpClient client = new OkHttpClient();

    private final String youtube = "https://www.youtube.com/watch?v=";

    private final AtomicInteger VideoNumber = new AtomicInteger();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length != 1 && command[0].equalsIgnoreCase("$yt")) {

            String keyword = "";

            for (int i = 1; i < command.length; i++) {
                keyword += command[i] + " ";
            }

            try {

                String youtubeVideo = search(keyword, 25);

                event.getChannel().sendMessage(youtubeVideo).queue();

            } catch (Exception ex) {
                event.getChannel().sendMessage("Something went wrong, please try with different search query.").queue();
            }

        }

    }

    private String search(String keyword, int maxResults) throws Exception {

        if (VideoNumber.get() == maxResults) {
            VideoNumber.set(0);
        }

        String URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&regionCode=US&maxResults=" + maxResults +
                "&q=" + keyword.replaceAll(" ", "_") + "&key=" + API_key();

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

        }

        JSONArray data = json.getJSONArray("items");

        int videoID = VideoNumber.incrementAndGet();

        String videoData = data.getJSONObject(videoID).getJSONObject("id").getString("videoId");

        if (!videoData.startsWith("{\"\":\"youtube#channel\",") || videoData.contains("playlistId")) {

            return youtube + videoData;

        }

        VideoNumber.set(0);

        return "Something went wrong...";
    }

    private String API_key() {

        return Tokens.YOUTUBE_API_KEY;

    }

}
