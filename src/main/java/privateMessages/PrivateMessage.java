package event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PrivateMessage extends ListenerAdapter {

    public static void sendPrivateMessage(User member, EmbedBuilder content) {
        member.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(content.build()).queue();

            channel.deleteMessageById(channel.getLatestMessageId()).queue();
        });
    }

    public static void sendPrivateMessage(User member, String content) {
        member.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(content).queue();

            channel.deleteMessageById(channel.getLatestMessageId()).queue();
        });
    }

    public static void sendPrivateMessage(User member, EmbedBuilder content, boolean removePreviousMessage) {
        member.openPrivateChannel().queue((channel) -> {
            if (removePreviousMessage) {
                channel.deleteMessageById(channel.getLatestMessageId()).queue();
            }
            channel.sendMessage(content.build()).queue();
        });
    }

    public static void sendPrivateMessage(Member member, EmbedBuilder embedBuilder, boolean removePreviousMessage) {
        member.getUser().openPrivateChannel().queue(channel -> {
            if (removePreviousMessage) {
                channel.deleteMessageById(channel.getLatestMessageId()).queue();
            }
            channel.sendMessage(embedBuilder.build()).queue();
        });
    }
    
}
