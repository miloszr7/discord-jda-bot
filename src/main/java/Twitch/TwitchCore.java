package Twitch;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.awt.*;

public class TwitchCore extends ListenerAdapter {

    private final OnlineChannel channel = new OnlineChannel();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length == 2 && command[0].equalsIgnoreCase("$twitch")) {

                GenerateToken.generate();

                JSONArray profileInfo = channel.getUserInfo(command[1]);
                JSONArray livestreamInfo = channel.getStreamInfo(command[1]);

                String thumbnail = profileInfo.getJSONObject(0).getString("profile_image_url");

                System.out.println(profileInfo);
                System.out.println(livestreamInfo);

                if (!livestreamInfo.isEmpty()) {

                    event.getChannel().sendTyping().queue();

                    event.getChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.decode("#9147FE"))
                                    .setTitle(command[1] + " is currently LIVE!")
                                    .setThumbnail(thumbnail)
                                    .build()
                    ).queue();

                } else {

                    event.getChannel().sendTyping().queue();

                    event.getChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.decode("#9147FE"))
                                    .setImage(profileInfo.getJSONObject(0).getString("offline_image_url"))
                                    .setTitle(command[1] + " is currently OFFLINE")
                                    .build()
                    ).queue();

                }


            }

        } catch (Exception ex) {

            System.out.println(ex.getMessage());

            event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setDescription("Sorry, I couldn't find someone you were looking for.")
                    .build()
            ).queue();

        }

    }
}
