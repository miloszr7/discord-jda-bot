package music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Premium members can use any source of music which is:
 * YouTube, SoundCloud, Bandcamp, Vimeo, Twitch streams, Local files, HTTP URLs
 * Being able to change volume,
 */

public class MusicManager {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }


    public boolean inVoiceChannel(GuildMessageReceivedEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();

        return audioManager.isConnected();
    }

    public boolean userConnected(GuildMessageReceivedEvent event) {
        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();

        return memberVoiceState.inVoiceChannel();
    }

    public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    private void skip(GuildMessageReceivedEvent event) {

        if (!inVoiceChannel(event) && !userConnected(event)) {
            return;
        }

        System.out.println("All good");

        Guild guild = event.getGuild();
        Member member = event.getMember();

        HashMap<String, String> voters = new HashMap<>();

        int connectedMembers = event.getChannel().getMembers().size();

        // start vote with > X users in the channel
        if (connectedMembers > 4) {

            // check if user voted
            for (Map.Entry<String, String> entry : voters.entrySet()) {
                // select users from the same server
                if (entry.getValue().equals(guild.getId())) {
                    // if user voted
                    if (entry.getKey().equals(member.getId())) {
                        return;
                    }
                }
            }

            // add single user to the list if used the command along with server id
            voters.put(member.getId(), guild.getId());

            int voteCount = 0;

            // count votes
            for (Map.Entry<String, String> entry : voters.entrySet()) {
                if (entry.getValue().equals(guild.getId())) {
                    if (entry.getKey().equals(member.getId())) {
                        voteCount++;
                    }
                }
            }

            if (voteCount > (connectedMembers / 2)) {
                skipTrack(event.getChannel());
                // remove values from the list
            }

        } else {
            skipTrack(event.getChannel());
        }

        System.out.println(voters);

    }

    public void pause(TextChannel channel, AudioPlayer audioPlayer) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        musicManager.scheduler.onPlayerPause(audioPlayer);

        channel.sendMessage(
                new EmbedBuilder()
                        .setTitle("Paused")
                        .setDescription(audioPlayer.getPlayingTrack().getInfo().title)
                        .build()
        ).queue();
    }

    public void clearQueue(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        musicManager.scheduler.clearQueue();

        channel.sendMessage("Queue cleared").queue();
    }

    public void showQueue(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        StringBuilder message = new StringBuilder();

        int count = 0;

        for (String name : musicManager.scheduler.showQueue()) {
            count++;
            message.append("**").append(count).append("**. ").append(name).append("\n");
        }

        if (count > 0) {
            channel.sendMessage(
                    new EmbedBuilder()
                            .setTitle("Current queue")
                            .setDescription(message)
                            .build()
            ).queue();
        } else {
            channel.sendMessage("There is no tracks in the queue.").queue();
        }
    }

    public void resume(TextChannel channel, AudioPlayer audioPlayer) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        musicManager.scheduler.onPlayerResume(audioPlayer);

        channel.sendMessage("Resumed **" + audioPlayer.getPlayingTrack().getInfo().title + "**").queue();
    }

    public void loadAndPlay(final TextChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                channel.sendMessage(
                        new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setTitle("Added to queue")
                        .setDescription("[" + track.getInfo().title + "]" + "(" + track.getInfo().uri + ")")
                        .setThumbnail("https://i.imgur.com/4b1gh6D.png")
                        .build()
                ).queue();

                play(channel.getGuild(), musicManager, track);

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                channel.sendMessage("Adding to queue `" + firstTrack.getInfo().title + "`\n Playlist `" + playlist.getName() + "`").queue();

                play(channel.getGuild(), musicManager, firstTrack);

            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    private void loadAndPlay(GuildMessageReceivedEvent event, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getChannel().getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {

                play(event.getChannel().getGuild(), musicManager, track);

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                play(event.getChannel().getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                System.out.println("No matches for provided music track.");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                System.out.println("Load failed.");
            }
        });
    }

    public void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public void setVolume(GuildMessageReceivedEvent event, GuildMusicManager musicManager, int value, int maxVolume) {
        if (value > maxVolume) {

            event.getChannel().sendMessage("Maximum volume is **" + maxVolume + "**%").queue();

        } else if (value <= 1) {

            event.getChannel().sendMessage("Minimum volume is **1**%").queue();

        } else {

            musicManager.player.setVolume(value);

            event.getChannel().sendMessage("Volume: **" + musicManager.player.getVolume() + "**%").queue();

        }
    }

    public void stopMusic(GuildMusicManager musicManager) {
        musicManager.player.stopTrack();
    }

    public void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
    }

}
