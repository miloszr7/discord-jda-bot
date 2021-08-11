package games;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class eightBall extends ListenerAdapter {

    private final String[] messages = {
            "As I see it, yes.", "Ask again later.", "Better not tell you now.",
            "Cannot predict now.", "Concentrate and ask again.", "Don’t count on it.",
            "It is certain.", "It is decidedly so.", "Most likely.",
            "My reply is no.", "My sources say no.", "Outlook not so good.",
            "Outlook good.", "Reply hazy, try again.", "Signs point to yes.",
            "Very doubtful.", "Without a doubt.", "Yes.",
            "Yes – definitely.", "You may rely on it."
    };

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        int random = (int) (Math.random() * messages.length);

        if (command.length != 1 && command[0].equalsIgnoreCase("$8ball")) {

            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " **" + messages[random] + "**").queue();

        }

    }
}
