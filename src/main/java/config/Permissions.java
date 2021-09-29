package config;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Permissions extends ListenerAdapter {
    
    //
    
    public static boolean hasRequiredPermission(GuildMessageReceivedEvent event, Permission permission) {

        Member member = event.getMember();

        if (!member.hasPermission(permission)) {
            event.getChannel().sendMessage("You need **" +permission.getName()+ "** permission to use this command.").queue();
            return false;
        }

        return true;
    }
    
    //

    public boolean ADMINISTRATOR(Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }

    public boolean ADMINISTRATOR(GuildMessageReceivedEvent event, Member member) {

        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            return true;
        } else {
            event.getChannel().sendMessage("You need **Administrator** permission to use this command.").queue();
            return false;
        }

    }

    public boolean BAN_MEMBERS(Member member) {
        return member.hasPermission(Permission.BAN_MEMBERS);
    }

    public boolean BAN_MEMBERS(GuildMessageReceivedEvent event, Member member) {

        if (member.hasPermission(Permission.BAN_MEMBERS)) {
            return true;
        } else {
            event.getChannel().sendMessage("You need **Ban Members** permission to use this command.").queue();
            return false;
        }

    }

    public boolean BAN_MEMBERS(SlashCommandEvent event, Member member) {

        if (member.hasPermission(Permission.BAN_MEMBERS)) {
            return true;
        } else {
            event.getInteraction().reply("You need **Ban Members** permission to use this command.").setEphemeral(true).queue();
            return false;
        }

    }

    public boolean KICK_MEMBERS(Member member) {
        return member.hasPermission(Permission.KICK_MEMBERS);
    }

    public boolean KICK_MEMBERS(GuildMessageReceivedEvent event, Member member) {

        if (member.hasPermission(Permission.KICK_MEMBERS)) {
            return true;
        } else {
            event.getChannel().sendMessage("You need **Kick Members** permission to use this command.").queue();
            return false;
        }

    }

    public boolean KICK_MEMBERS(SlashCommandEvent event, Member member) {

        if (member.hasPermission(Permission.KICK_MEMBERS)) {
            return true;
        } else {
            event.getInteraction().reply("You need **Kick Members** permission to use this command.").queue();
            return false;
        }

    }

    public boolean MANAGE_CHANNELS(Member member) {
        return member.hasPermission(Permission.MANAGE_CHANNEL);
    }

    public boolean MANAGE_CHANNELS(GuildMessageReceivedEvent event, Member member) {

        if (member.hasPermission(Permission.MANAGE_CHANNEL)) {
            return true;
        } else {
            event.getChannel().sendMessage("You need **Manage Channels** permission to use this command.").queue();
            return false;
        }

    }

    public boolean MANAGE_CHANNELS(SlashCommandEvent event, Member member) {

        if (member.hasPermission(Permission.MANAGE_CHANNEL)) {
            return true;
        } else {
            event.getInteraction().reply("You need **Manage Channels** permission to use this command.").setEphemeral(true).queue();
            return false;
        }

    }

    public boolean NICKNAME_CHANGE(Member member) {
        return member.hasPermission(Permission.NICKNAME_CHANGE);
    }

    public boolean NICKNAME_CHANGE(GuildMessageReceivedEvent event, Member member) {

        if (member.hasPermission(Permission.NICKNAME_CHANGE)) {
            return true;
        } else {
            event.getChannel().sendMessage("You need **Change Nickname** permission to use this command.").queue();
            return false;
        }

    }

    public boolean MANAGE_SERVER(Member member) {
        return member.hasPermission(Permission.MANAGE_SERVER);
    }

    public boolean MANAGE_SERVER(GuildMessageReceivedEvent event, Member member) {

        if (member.hasPermission(Permission.MANAGE_SERVER)) {
            return true;
        } else {
            event.getChannel().sendMessage("You need **Manage Server** permission to use this command.").queue();
            return false;
        }

    }

    public boolean MANAGE_ROLES(Member member) {
        return member.hasPermission(Permission.MANAGE_ROLES);
    }

    public boolean MANAGE_ROLES(GuildMessageReceivedEvent event, Member member) {

        if (member.hasPermission(Permission.MANAGE_ROLES)) {
            return true;
        } else {
            event.getChannel().sendMessage("You need **Manage Roles** permission to use this command.").queue();
            return false;
        }

    }

    public boolean CREATE_INSTANT_INVITE(Member member) {
        return member.hasPermission(Permission.CREATE_INSTANT_INVITE);
    }

}
