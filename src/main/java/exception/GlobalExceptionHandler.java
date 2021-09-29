package exception;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GlobalExceptionHandler {

    private static final String serverId = "";
    private static final String textChannelId = "";

    /**
     * Handle normal exception without discord interaction.
     * @param name Name of the class or exception tag
     * @param exception Exception
     *
     */

    public static void handleException(String name, Exception exception) {

        exception.printStackTrace();

        System.out.println("[" + name + "] :: " + exception.getMessage());

    }

    /**
     * Handle exception and send message to discord channel.
     * @param event SlashCommandEvent to interact with message
     * @param name Name of the class or exception tag
     * @param exception Exception
     *
     */

    public static void handleException(SlashCommandEvent event, String name, Exception exception) {

        String messageFormat = "[" + name + "] :: " + exception.getMessage();

        System.out.println(messageFormat);

        event.getJDA().getGuildById(serverId).getTextChannelById(textChannelId)
                .sendMessage(messageFormat).queue();

    }

    /**
     * Handle exception and send message to discord channel.
     * @param event GuildMessageReceivedEvent to interact with message
     * @param name Name of the class or exception tag
     * @param exception Exception
     *
     */

    public static void handleException(GuildMessageReceivedEvent event, String name, Exception exception) {

        System.out.println("[" + name + "] :: " + exception.getMessage());

        event.getJDA().getGuildById(serverId).getTextChannelById(textChannelId)
                .sendMessage(exception.getMessage()).queue();

    }

}
