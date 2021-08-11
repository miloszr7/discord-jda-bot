

package privateMessages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;

public class PrivateMessage extends ListenerAdapter {

    public static void sendPrivateMessage(User member, EmbedBuilder content) {
        member.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content.build()).queue();

            String messageID = channel.getLatestMessageId();

            channel.deleteMessageById(messageID).queue();

        });
    }

    public static void sendPrivateMessage(User member, String content) {
        member.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queue();

            String messageID = channel.getLatestMessageId();

            channel.deleteMessageById(messageID).queue();

        });
    }

    public static void sendPrivateMessage(User member, EmbedBuilder content, boolean removeLatestMessage) {
        member.openPrivateChannel().queue((channel) ->
        {
            if (removeLatestMessage) {
                channel.sendMessage(content.build()).queue();

                String messageID = channel.getLatestMessageId();

                channel.deleteMessageById(messageID).queue();
            } else {
                channel.sendMessage(content.build()).queue();
            }
        });
    }

    public static void sendPrivateMessage(Member member, EmbedBuilder embedBuilder, boolean removeLatestMessage) {
        member.getUser().openPrivateChannel().queue(channel ->
        {
            if (removeLatestMessage) {
                channel.sendMessage(embedBuilder.build()).queue();

                String messageID = channel.getLatestMessageId();

                channel.deleteMessageById(messageID).queue();
            } else {
                channel.sendMessage(embedBuilder.build()).queue();
            }
        });
    }

    public static void sendPrivateMessage(User author, File file, String filename, boolean removeLatestMessage) {

        author.openPrivateChannel().queue(privateChannel -> {

            if (removeLatestMessage) {

                privateChannel.sendFile(file, filename).queue();

            }

        });

    }
}

