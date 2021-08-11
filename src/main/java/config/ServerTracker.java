package config;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.discordbots.api.client.DiscordBotListAPI;
import org.jetbrains.annotations.NotNull;

public class ServerTracker extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        DiscordBotListAPI topGG = new DiscordBotListAPI.Builder()
                .token(Tokens.TOP_GG_API_KEY)
                .botId("")
                .build();

        topGG.setStats(event.getJDA().getGuilds().size());

    }
}
