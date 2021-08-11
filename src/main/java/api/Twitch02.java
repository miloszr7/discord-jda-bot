package api;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Twitch02 extends ListenerAdapter {

    private final OkHttpClient client = new OkHttpClient();

    private String channel;
    private String title;
    private String watching;
    private String avatar;
    private String gameID;
    private String game;

    private JSONArray jsonArray_stream;
    private JSONArray jsonArray_user;
    private JSONArray jsonArray_game;

    protected String ClientID() { return ""; }
    protected String AccessToken() { return ""; }


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        try {

            String[] channelName = event.getMessage().getContentRaw().split(" ");

            if (channelName.length == 2 && channelName[0].equalsIgnoreCase("$twitch")) {

                String username = channelName[1];

                String streamURL = "https://api.twitch.tv/helix/streams?user_login=" + username;
                String userURL = "https://api.twitch.tv/helix/users?login=" + username;

                Request request = new Request.Builder()
                        .url(streamURL)
                            .addHeader("Authorization", AccessToken())
                                .addHeader("Client-Id", ClientID())
                                    .get()
                                        .build();

                String content = "";

                JSONObject json = new JSONObject();

                try (Response response = client.newCall(request).execute()) {

                    content = Objects.requireNonNull(response.body()).string();

                    json = new JSONObject(content);

                }

                System.out.println(json);

                    //jsonArray_stream = json_stream.getJSONArray("data");
                    //jsonArray_user = json_user.getJSONArray("data");

                    if (jsonArray_stream.isEmpty()) {

                        EmbedBuilder info = new EmbedBuilder();

                        info.setColor(Color.decode("#391C65"));
                        info.setTitle("**Couldn't find any active live streams**");
                        info.setDescription("Whether channel name is incorrect or user is offline at this moment.");

                        event.getChannel().sendMessage(info.build()).queue();
                    }

                    channel = jsonArray_stream.getJSONObject(0).get("user_name").toString();
                    title = jsonArray_stream.getJSONObject(0).get("title").toString();
                    watching = jsonArray_stream.getJSONObject(0).get("viewer_count").toString();
                    avatar = jsonArray_user.getJSONObject(0).get("profile_image_url").toString();
                    gameID = jsonArray_stream.getJSONObject(0).get("game_id").toString();





                    String gameURL = "https://api.twitch.tv/helix/games?id=" + gameID;

                    URL Game = new URL((gameURL));

                    HttpURLConnection connectionGame = (HttpURLConnection) Game.openConnection();

                    connectionGame.setRequestMethod("GET");
                    connectionGame.setRequestProperty("Authorization", AccessToken());
                    connectionGame.setRequestProperty("Client-ID", ClientID());

                    int gameResponseCode = connectionGame.getResponseCode();

                    if (gameResponseCode == HttpURLConnection.HTTP_OK) {

                        InputStream is_game = connectionGame.getInputStream();

                        BufferedReader rd_game = new BufferedReader(new InputStreamReader(is_game, StandardCharsets.UTF_8));

                        String jsonText_game= readAll(rd_game);

                        JSONObject json_game = new JSONObject(jsonText_game);

                        is_game.close();

                        jsonArray_game = json_game.getJSONArray("data");

                        game = jsonArray_game.getJSONObject(0).get("name").toString();

                    }

                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    embedBuilder.setTitle("**" + channel + "** is LIVE on Twitch", "http://twitch.tv/" + channel);
                        embedBuilder.setThumbnail(avatar);
                        embedBuilder.setColor(Color.decode("#6400FF"));
                        //embedBuilder.addField("", "*" + title + "*", false);
                        //embedBuilder.addField("", "\uD83D\uDCA5 **" + game + "**", false);
                       // embedBuilder.addField("", "\uD83C\uDF10 ** " + watching + "** watching now.", false);
                        embedBuilder.setDescription(
                                "*" + title + "*" + "\n" + "\n" +
                                "Streaming **" + game + "** with" + "\n" +
                                ":globe_with_meridians:  ** " + watching + "** viewers.");

                    event.getChannel().sendMessage(embedBuilder.build()).queue();

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
