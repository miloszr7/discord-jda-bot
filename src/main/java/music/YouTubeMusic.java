package music;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class YouTubeMusic {

    private final OkHttpClient client = new OkHttpClient();

    private final String youtubeLink = "https://www.youtube.com/watch?v=";

    private final AtomicInteger VideoNumber = new AtomicInteger();

    public String search(String keyword) throws Exception {

        int maxResults = 20;

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

            if (VideoNumber.get() == maxResults) {
                VideoNumber.set(1);
            }

            int videoID = VideoNumber.incrementAndGet();

            // get first search result cuz is the best lol
            String videoNoRandom = data.getJSONObject(0).getJSONObject("id").getString("videoId");

            if (!videoNoRandom.startsWith("{\"\":\"youtube#channel\",") || videoNoRandom.contains("playlistId")) {

                return youtubeLink + videoNoRandom;

            }

            return "-";
    }

    private String API_key() {

        return "AIzaSyCAqBXFUFocPDE-DjjZHscRdMqkxrgeJEs";

    }

}
