package Twitch;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;

public class OnlineChannel {

    private final OkHttpClient client = new OkHttpClient();

    private JSONArray getJSONData(String websiteURL) throws Exception {

        URL website = new URL(websiteURL);

        Request request = new Request.Builder()
                .url(website)
                .header("Authorization", "Bearer " + Credentials.TWITCH_TOKEN)
                .addHeader("client-id", Credentials.TWITCH_CLIENT_ID)
                .get()
                .build();

        String content = "";

        JSONObject json;

        try (Response response = client.newCall(request).execute()) {

            content = Objects.requireNonNull(response.body()).string();

            json = new JSONObject(content);

        }

        return json.getJSONArray("data");

    }

    public JSONArray getUserInfo(String channelName) throws Exception {

        return getJSONData("https://api.twitch.tv/helix/users?login=" + channelName);

    }

    public JSONArray getStreamInfo(String channelName) throws Exception {

        return getJSONData("https://api.twitch.tv/helix/streams?user_login=" + channelName);

    }

}
