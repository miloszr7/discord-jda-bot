package Twitch;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;

public class TwitchCore extends ListenerAdapter {

    private final OnlineChannel channel = new OnlineChannel();

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        try {

            if (event.getName().equals("twitch")) {

                String target = event.getOption("username").getAsString();

                GenerateToken.generate();

                JSONArray profileInfo = channel.getUserInfo(target);
                JSONArray livestreamInfo = channel.getStreamInfo(target);

                String thumbnail = profileInfo.getJSONObject(0).getString("profile_image_url");

                if (!livestreamInfo.isEmpty()) {

                    String username = livestreamInfo.getJSONObject(0).getString("user_name");
                    String userLogin = livestreamInfo.getJSONObject(0).getString("user_login");
                    String game = livestreamInfo.getJSONObject(0).getString("game_name");
                    String title = livestreamInfo.getJSONObject(0).getString("title");
                    String startedAt = livestreamInfo.getJSONObject(0).getString("started_at");

                    String preview = "https://static-cdn.jtvnw.net/previews-ttv/live_user_"+userLogin+"-1920x1080.jpg";

                    int viewers = livestreamInfo.getJSONObject(0).getInt("viewer_count");

                    Instant began = Instant.ofEpochSecond(OffsetDateTime.parse(startedAt).toEpochSecond());
                    Instant now = Instant.ofEpochSecond(event.getTimeCreated().toEpochSecond());

                    Duration duration = Duration.between(began, now);

                    EmbedBuilder message = new EmbedBuilder();
                    message.setTitle(username, "https://www.twitch.tv/" + username);
                    message.setColor(Color.decode("#9147FE"));
                    message.setDescription(title);
                    message.addField("Viewers", ""+viewers, true);
                    message.setThumbnail(thumbnail);
                    message.setFooter(game);
                    message.setImage(preview);

                    if (duration.toMinutes() > 60) {
                        long hours = duration.toMinutes() / 60;
                        long minutes = duration.toMinutes() % 60;

                        message.addField("Live", hours + "h " + minutes + "m", true);
                    } else {
                        message.addField("Live", duration.toMinutes() + "m", true);
                    }

                    event.getInteraction().replyEmbeds(message.build()).queue();

                } else {

                    event.getInteraction().replyEmbeds(
                            new EmbedBuilder()
                                    .setColor(Color.decode("#9147FE"))
                                    .setImage(profileInfo.getJSONObject(0).getString("offline_image_url"))
                                    .setTitle(target + " is currently OFFLINE")
                                    .build()
                    ).queue();

                }

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
