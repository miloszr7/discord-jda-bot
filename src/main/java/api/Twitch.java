package api;

import privateMessages.PrivateMessage;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Twitch extends ListenerAdapter {

    protected static String DiscordWebhooksURL() {

        return "";

    }

    protected static String ClientID() {

        return "";

    }

    protected static String ClientSecret() {

        return "";

    }

    protected static String accessToken() {

        return "";

    }

    public static String channelName = "";

    // Variables for Twitch Streams
    public static String Username = "";
    public static String Title = "";
    public static String Viewers = "";
    public static String LiveStatus = "";
    //

    // Variables for Users Data
    public static String display_name = "";
    public static String broadcaster_type = ""; // - User’s broadcaster type: "partner", "affiliate", or "".
    public static String profile_image_url = "";
    public static String description = "";
    public static int view_count = 0; // - Total number of views of the user’s channel.
    //

    public static JSONArray jsonArray_stream;
    public static JSONArray jsonArray_user;

    public static List<String> offline = new ArrayList<>();
    public static List<String> avatars = new ArrayList<>();

    /*

        From original twitch URL we need to get channel name in order to proceed API request.
        API using completely different URL, so we need channel name that will be replaced in a last index.
        Example: Twitch URL = https://www.twitch.tv/superName | API URL = https://api.twitch.tv/helix/users?login=superName
        All we have to do it replace URL except the last index that shows channel name.

        API will only work for verified twitch accounts that are located in database.
        Application automatically and temporally removes offline channels, but later they are placed back if they're online.
        Thanks to this application won't stuck on retrieving data from website and placing them into JSON.

    */

    @Deprecated
    public void PrepareTwitchAPI() throws Exception {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {

                    String url = "jdbc:sqlite:database.db";

                    List<String> URLS = new ArrayList<>();

                    Connection databaseConnection = DriverManager.getConnection(url);

                    Statement st = databaseConnection.createStatement();

                    // From verified table we take our twitch URL.
                    ResultSet CollectTwitchURL = st.executeQuery("SELECT TwitchLink FROM verified");

                    while (CollectTwitchURL.next()) {

                        URLS.add(CollectTwitchURL.getString("TwitchLink"));

                    }

                    // We temporally remove loaded list of offline twitch channels.

                    for (String ch : offline) {

                        URLS.remove(ch);

                    }

                    for (String channel : URLS) {

                        // Replacement of twitch URL.
                        String twitchName = channel.replace("https://www.twitch.tv/", "");

                        // Now we need to add replaced String to API URL.
                        String StreamURL = "https://api.twitch.tv/helix/streams?user_login=" + twitchName;
                        String UserURL = "https://api.twitch.tv/helix/users?login=" + twitchName;

                        // Set up our previous URL.
                        URL URL_stream = new URL((StreamURL));
                        URL URL_user = new URL((UserURL));

                        HttpURLConnection connection_stream = (HttpURLConnection) URL_stream.openConnection();
                        HttpURLConnection connection_user = (HttpURLConnection) URL_user.openConnection();

                        // Send request to the website to get JSON data.
                        connection_stream.setRequestMethod("GET");
                        connection_user.setRequestProperty("Accept", "application/vnd.twitchtv.v5+json");
                        connection_stream.setRequestProperty("Authorization", accessToken());
                        connection_stream.setRequestProperty("Client-Id", ClientID());

                        connection_user.setRequestMethod("GET");
                        connection_user.setRequestProperty("Accept", "application/vnd.twitchtv.v5+json");
                        connection_user.setRequestProperty("Authorization", accessToken());
                        connection_user.setRequestProperty("Client-Id", ClientID());

                        int responseCode_stream = connection_stream.getResponseCode();
                        int responseCode_user = connection_user.getResponseCode();

                        System.out.println("GET Response Code for Stream :: " + responseCode_stream);
                        System.out.println("GET Response Code for User :: " + responseCode_user);

                        if (responseCode_stream == HttpURLConnection.HTTP_OK && responseCode_user == HttpURLConnection.HTTP_OK) {

                            // Here we simply collect our data and place it inside String.
                            InputStream is_stream = connection_stream.getInputStream();
                            InputStream is_user = connection_user.getInputStream();

                            BufferedReader rd_stream = new BufferedReader(new InputStreamReader(is_stream, StandardCharsets.UTF_8));
                            String jsonText_stream = readAll(rd_stream);

                            BufferedReader rd_user = new BufferedReader(new InputStreamReader(is_user, StandardCharsets.UTF_8));
                            String jsonText_user = readAll(rd_user);

                            // This is our collected data.
                            JSONObject json_stream = new JSONObject(jsonText_stream);
                            JSONObject json_user = new JSONObject(jsonText_user);

                            // Print out JSON.
                            //System.out.println(json_stream.toString());
                            //System.out.println(json_user.toString());

                            // Close reader.
                            is_stream.close();
                            is_user.close();

                            jsonArray_stream = json_stream.getJSONArray("data");
                            jsonArray_user = json_user.getJSONArray("data");

                            // Show offline channels and place them inside list.
                            if (jsonArray_stream.isEmpty()) {

                                System.out.println(channel + " is OFFLINE");

                                offline.add(channel);

                            }

                            Username = jsonArray_stream.getJSONObject(0).get("user_name").toString();
                            Title = jsonArray_stream.getJSONObject(0).get("title").toString();
                            LiveStatus = jsonArray_stream.getJSONObject(0).get("type").toString();
                            Viewers = jsonArray_stream.getJSONObject(0).get("viewer_count").toString();

                            display_name = jsonArray_user.getJSONObject(0).get("display_name").toString();
                            broadcaster_type = jsonArray_user.getJSONObject(0).get("broadcaster_type").toString();
                            profile_image_url = jsonArray_user.getJSONObject(0).get("profile_image_url").toString();
                            description = jsonArray_user.getJSONObject(0).get("description").toString();
                            view_count = (Integer) jsonArray_user.getJSONObject(0).get("view_count");

                            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                            System.out.println("NAME: " + Username);
                            System.out.println("TITLE: " + Title);
                            System.out.println("VIEWERS: " + Viewers);
                            System.out.println("PROFILE IMAGE: " + profile_image_url);
                            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                            System.out.println("");


                            WebhookClient client = WebhookClient.withUrl(DiscordWebhooksURL());

                            WebhookEmbed.EmbedTitle embedTitle = new WebhookEmbed.EmbedTitle("**" + Username + "** is now streaming on Twitch!", channel);

                            @NotNull WebhookEmbed webhookEmbed = new WebhookEmbedBuilder()
                                    .setThumbnailUrl(profile_image_url)
                                    .setColor(5701887)
                                    .setTitle(embedTitle)
                                    .setDescription("*" +
                                            Title + "*" + "\n" +
                                                    "\n" +
                                                    "\n" +
                                                    "**" + Viewers + "** watching now."
                                    )
                                    .build();

                            client.send(webhookEmbed)
                                    .thenAccept((
                                            message -> System.out.println("")
                                    ));



                        } else {
                            System.out.println("GET request failed :: " + connection_stream.getResponseMessage());
                        }
                    }


                } catch (Exception e) {
                    e.getMessage();
                }

            }

        };


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


        // Execute function in specified period of time.
        // Period is defined in seconds but it can be change at any time.

        executor.scheduleAtFixedRate(runnable,0,5,TimeUnit.SECONDS);

    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

        String[] space = event.getMessage().getContentRaw().split(" ");

        if (space.length == 2 && space[0].equalsIgnoreCase("$streamer")) {

            PrivateMessage directMessage = new PrivateMessage();

            EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("Twitch Verification");
            builder.setColor(Color.decode("#7800FF"));
            builder.setDescription("**Thank you for your Twitch URL." + "\n" + "We will verify your partnership soon**.");

            directMessage.sendPrivateMessage(Objects.requireNonNull(event.getMember()).getUser(), builder);

            String url = "jdbc:sqlite:database.db";

            String author = event.getMessage().getAuthor().getName();
            String VerificationID = event.getMessage().getId();
            String authorID = event.getMessage().getAuthor().getId();

            channelName = space[1];

            try {

                Connection connection = DriverManager.getConnection(url);

                Statement st = connection.createStatement();

                String sql = "INSERT INTO twitch VALUES (null, '"+channelName+"', '"+VerificationID+"', '"+author+"', '"+authorID+"')";

                st.executeUpdate(sql);

            } catch (Exception e) {
                e.getMessage();
            }

            event.getMessage().delete().queue();

            System.out.println("[Twitch] New verification has showed up :: ID: " + event.getMessage().getId());

        } else if (space.length == 2 && space[0].equalsIgnoreCase("$verify")) {
            event.getMessage().delete().queue();

            PrivateMessage directMessage = new PrivateMessage();

            String mentioned = space[1];

            EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("Twitch Verification");
            builder.setColor(Color.decode("#7800FF"));
            builder.setDescription("**Your partnership has been successfully approved**!");

            directMessage.sendPrivateMessage(event.getMember().getUser(), builder, true);

            String url = "jdbc:sqlite:database.db";

            try {

                // Before we remove verified author we need to gather his data to send it to another table.

                Connection connection = DriverManager.getConnection(url);

                Statement st = connection.createStatement();

                ResultSet gatherData = st.executeQuery("SELECT * FROM twitch WHERE TwitchLink = '"+mentioned+"'");

                String TwitchLink = "";
                String VerificationID = "";
                String author = "";
                String authorID = "";

                while (gatherData.next()) {

                    TwitchLink = gatherData.getString("TwitchLink");
                    VerificationID = gatherData.getString("VerificationID");
                    author = gatherData.getString("Author");
                    authorID = gatherData.getString("AuthorID");

                }

                String removeFromTwitch = "DELETE FROM twitch WHERE TwitchLink = '"+mentioned+"'";

                st.executeUpdate(removeFromTwitch);

                // Here we add gathered data to a new table.

                String addToVerified = "INSERT INTO verified VALUES (null, '"+TwitchLink+"', '"+VerificationID+"', '"+author+"', '"+authorID+"')";

                st.executeUpdate(addToVerified);

                System.out.println("[Twitch] Verification has been approved for ID: " + event.getAuthor().getId());

            } catch (Exception e) {
                e.getMessage();
            }

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
