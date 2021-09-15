package music;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class PlayerControl extends ListenerAdapter {

    private MusicManager musicHandler;

    private final char prefix = '!';

    public PlayerControl() {
        this.musicHandler = new MusicManager();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        GuildMusicManager musicManager;

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length != 1) {
            if (musicHandler.userConnected(event) && musicHandler.inVoiceChannel(event)) {

                if (command[0].equalsIgnoreCase(prefix + "play")) {

                    StringBuilder keyword = new StringBuilder();

                    for (int i = 1; i < command.length; i++) {
                        keyword.append(command[i]).append(" ");
                    }

                    try {

                        if (keyword.length() > 1) {
                            musicHandler.loadAndPlay(event.getChannel(), YoutubeMusic.search(keyword.toString()));
                        } else if (keyword.length() == 1) {
                            musicHandler.loadAndPlay(event.getChannel(), String.valueOf(command[1]));
                        }

                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                }

            }

            if (command[0].equalsIgnoreCase(prefix + "volume")) {

                musicManager = getGuildMusicManager(event);

                int volume = Integer.parseInt(command[1]);

                musicHandler.setVolume(event, musicManager, volume, 150);

            }

            if (command[0].equalsIgnoreCase(prefix + "repeat") && command[1].equalsIgnoreCase("on")) {

                musicManager = getGuildMusicManager(event);

                musicManager.scheduler.setRepeating(!musicManager.scheduler.isRepeating());
                event.getChannel().sendMessage("Player was set to **repeat**").queue();

            } else if (command[0].equalsIgnoreCase(prefix + "repeat") && command[1].equalsIgnoreCase("off")) {

                musicManager = getGuildMusicManager(event);

                musicManager.scheduler.setRepeating(false);
                event.getChannel().sendMessage("Player was set to **no repeat**").queue();

            }
        }

        if (command.length == 1) {
            if (command[0].equalsIgnoreCase(prefix + "join")) {

                if (musicHandler.inVoiceChannel(event)) {
                    event.getChannel().sendMessage("I'm already in the voice channel.").queue();
                    return;
                }

                if (!musicHandler.userConnected(event)) {
                    event.getChannel().sendMessage("You have to join voice channel.").queue();
                    return;
                }

                AudioManager audioManager = event.getGuild().getAudioManager();

                audioManager.openAudioConnection(event.getMember().getVoiceState().getChannel());
            }

            if (command[0].equalsIgnoreCase(prefix + "stop")) {
                musicManager = getGuildMusicManager(event);

                musicHandler.stopMusic(musicManager);

                AudioManager audioManager = event.getGuild().getAudioManager();

                audioManager.closeAudioConnection();
            }

            if (command[0].equalsIgnoreCase(prefix + "pause")) {
                musicManager = getGuildMusicManager(event);

                musicHandler.pause(event.getChannel(), musicManager.player);
            }

            if (command[0].equalsIgnoreCase(prefix + "resume")) {
                musicManager = getGuildMusicManager(event);

                musicHandler.resume(event.getChannel(), musicManager.player);
            }

            if (command[0].equalsIgnoreCase(prefix + "queue")) {
                musicHandler.showQueue(event.getChannel());
            }

            if (command[0].equalsIgnoreCase(prefix + "clearqueue")) {
                musicHandler.clearQueue(event.getChannel());
            }

            if (command[0].equalsIgnoreCase(prefix + "skip")) {
                musicHandler.skipTrack(event.getChannel());
            }

            if (command[0].equalsIgnoreCase(prefix + "nowplaying")) {
                musicManager = getGuildMusicManager(event);

                event.getChannel().sendMessage("Now playing: **" + musicManager.player.getPlayingTrack().getInfo().title + "**").queue();
            }

        }

    }

    private GuildMusicManager getGuildMusicManager(@NotNull GuildMessageReceivedEvent event) {
        return musicHandler.getGuildAudioPlayer(event.getChannel().getGuild());
    }
}
