package music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MusicHandler extends ListenerAdapter {

    private final Premium premium = new Premium();

    private final YouTubeMusic youTubeMusic = new YouTubeMusic();

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicHandler() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }


    private boolean inVoiceChannel(GuildMessageReceivedEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();

        return audioManager.isConnected();
    }

    private boolean memberConnected(GuildMessageReceivedEvent event) {
        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();

        return memberVoiceState.inVoiceChannel();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().equalsIgnoreCase("$join") && premium.isPremium(event.getAuthor().getId())) {

            if (inVoiceChannel(event)) {
                event.getChannel().sendMessage("I'm already in the voice channel.").queue();
                return;
            }

            if (!memberConnected(event)) {
                event.getChannel().sendMessage("You have to join voice channel first.").queue();
                return;
            }

            AudioManager audioManager = event.getGuild().getAudioManager();

            audioManager.openAudioConnection(event.getMember().getVoiceState().getChannel());

        }

        if (event.getMessage().getContentRaw().startsWith("$play") && !inVoiceChannel(event) && !memberConnected(event)) {
            event.getChannel().sendMessage("We both have to be in the voice channel.").queue();
        }

        if (inVoiceChannel(event) && memberConnected(event)) {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length != 1 && command[0].equalsIgnoreCase("$play")) {

                StringBuilder keyword = new StringBuilder();

                for (int i = 1; i < command.length; i++) {
                    keyword.append(command[i]).append(" ");
                }

                try {

                    if (keyword.length() > 1) {
                        loadAndPlay(event.getChannel(), youTubeMusic.search(keyword.toString()));
                    } else if (keyword.length() == 1) {
                        loadAndPlay(event.getChannel(), String.valueOf(command[1]));
                    }

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

            }

            HashMap<Long, String> skipVoters = new HashMap<>();

            if (command[0].equalsIgnoreCase("$skip")) {

                AudioManager audioManager = event.getGuild().getAudioManager();

                // check how many people are listening to music (connected to same channel)
                int connectedMembers = audioManager.getConnectedChannel().getMembers().size();

                // request voting with x member in the channel
                int minVotingMembers = 4;

                if (connectedMembers > minVotingMembers) {

                    // at least 1/2 members need to vote for track skip
                    int requiredMembers = connectedMembers / 2;

                    // add voters to the list
                    skipVoters.put(event.getGuild().getIdLong(), event.getAuthor().getId());

                    // make sure everyone can vote just once per voting
                    int countVotes = 0;

                    if (skipVoters.containsValue(event.getAuthor().getId())) {
                        countVotes++;
                        if (countVotes > 1) {
                            System.out.println(event.getAuthor().getName() + " has already voted.");
                        }
                    }

                    event.getChannel().sendMessage("Skip current track **" + skipVoters.size() + "**/**" + requiredMembers + "**").queue();

                    int count = 0;

                    for (Map.Entry<Long, String> entry : skipVoters.entrySet()) {

                        if (entry.getKey() == event.getGuild().getIdLong()) {

                            count++;

                            if (count >= requiredMembers) {

                                skipVoters.remove(entry.getKey());

                                skipTrack(event.getChannel());

                            }
                        }
                    }


                } else {
                    skipTrack(event.getChannel());
                }

            }

            if (command.length == 2 && command[0].equalsIgnoreCase("$volume")) {

                GuildMusicManager musicManager = getGuildAudioPlayer(event.getChannel().getGuild());

                int volume = Integer.parseInt(command[1]);

                setVolume(event, musicManager, volume);

            }

            if (command[0].equalsIgnoreCase("$stop")) {

                GuildMusicManager musicManager = getGuildAudioPlayer(event.getChannel().getGuild());

                stopMusic(musicManager);

                AudioManager audioManager = event.getGuild().getAudioManager();

                audioManager.closeAudioConnection();

            }

        }

    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    private void loadAndPlay(final TextChannel channel, final String trackUrl) {
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

    public void loadAndPlay(GuildMessageReceivedEvent event, final String trackUrl) {
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


    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {

        musicManager.scheduler.queue(track);

    }

    private void setVolume(GuildMessageReceivedEvent event, GuildMusicManager musicManager, int value) {

        if (value > 110) {

            event.getChannel().sendMessage("Maximum volume is **110**%").queue();

        } else if (value <= 1) {

            event.getChannel().sendMessage("Minimum volume is **1**%").queue();

        } else {

            musicManager.player.setVolume(value);

            event.getChannel().sendMessage("Volume: **" + musicManager.player.getVolume() + "**%").queue();

        }

    }

    private void stopMusic(GuildMusicManager musicManager) {
        musicManager.player.stopTrack();
    }

    private void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
    }

    private static void connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                audioManager.openAudioConnection(voiceChannel);
                break;
            }
        }
    }

}
