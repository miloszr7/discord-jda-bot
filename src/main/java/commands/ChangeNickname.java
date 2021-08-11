package commands;

import config.Permissions;
import ratelimit.Cooldown;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ChangeNickname extends ListenerAdapter {

    public Permissions permissions = new Permissions();
    public Cooldown cooldown = new Cooldown();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length != 2 && command[0].equalsIgnoreCase("$nick")
                    && permissions.NICKNAME_CHANGE(event, event.getMember()) && !cooldown.hasCooldown(event.getMember())) {

                cooldown.activate(event, "$nick");

                Member member = event.getMessage().getMentionedMembers().get(0);

                String nickname = "";

                for (int i = 2; i < command.length; i++) {

                    nickname += command[i] + " ";

                }

                event.getGuild().modifyNickname(member, nickname).queue();

            }

        } catch (Exception e) {

            if (!e.getMessage().equalsIgnoreCase("Index: 0, Size: 0")) {
                event.getChannel().sendMessage(e.getMessage()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
            }
        }

    }
}
