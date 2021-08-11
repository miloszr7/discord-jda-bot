package Twitch;

import okhttp3.*;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;

class GenerateToken {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static void generate() throws Exception {

        OkHttpClient client = new OkHttpClient();

        URL website = new URL("https://id.twitch.tv/oauth2/token?client_id=CLIENT_ID&client_secret=CLIENT_SECRET&grant_type=client_credentials");

        String json = "";

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(website)
                .post(body)
                .build();

        String content ;

        JSONObject object ;

        try (Response response = client.newCall(request).execute()) {

            content = Objects.requireNonNull(response.body()).string();

            object = new JSONObject(content);

        }

        Credentials.TWITCH_TOKEN = object.getString("access_token");

    }

}
