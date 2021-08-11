package commands;

import config.Permissions;
import ratelimit.Cooldown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShowServerBans extends ListenerAdapter {

    private final Permissions permissions = new Permissions();
    private final Cooldown cooldown = new Cooldown();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            if (event.getMessage().getContentRaw().equals("$banlist")
                    && permissions.BAN_MEMBERS(event, event.getMember()) && !cooldown.hasCooldown(event.getMember())) {

                cooldown.activate(event, "$banlist");

                EmbedBuilder dashboard = new EmbedBuilder();

                dashboard.setTitle("[ " + event.getGuild().getName() + " ]");

                List<Guild.Ban> bans = event.getGuild().retrieveBanList().complete();

                StringBuilder description = new StringBuilder();

                int count = 0;

                for (Guild.Ban ban : bans) {

                    count++;

                    description.append(ban.getUser().getAsMention()).append("\t\t");

                    dashboard.setDescription(description);

                }

                if (count == 0) {

                    dashboard.setDescription("There is currently no bans");

                    event.getChannel().sendMessage(dashboard.build()).queue();

                } else if (count == 1) {

                    dashboard.setFooter("There is currently " + count + " banned account.");

                    event.getChannel().sendMessage(dashboard.build()).queue();

                } else if (count > 1) {

                    Emote warning = event.getJDA().getGuildById("").getEmoteById("");

                    Emoji emoji = Emoji.fromEmote(warning);

                    dashboard.setFooter("There is currently " + count + " banned accounts.");

                    event.getChannel().sendMessage(dashboard.build())
                            .setActionRow(Button.of(ButtonStyle.SECONDARY, "removeAllBans",  "UNBAN EVERYONE", emoji))
                    .queue();

                }

            }

        } catch (Exception e) {
            event.getChannel().sendMessage(e.getMessage()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
        }

    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {

        if (permissions.ADMINISTRATOR(event.getMember())
                && event.getComponentId().equals("removeAllBans")) {

            List<Guild.Ban> banList = event.getGuild().retrieveBanList().complete();

            for (Guild.Ban bans : banList) {
                event.getGuild().unban(bans.getUser().getId()).complete();
            }

            event.getInteraction().deferEdit().queue();

        }

    }
}
