package models;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StreamPlayers extends ListenerAdapter {

    private GuildMessageReactionAddEvent event;
    private String _GameKey;
    private String _P1;
    private String _P2;
    private String _messageID;

    public StreamPlayers() {}

    public StreamPlayers(GuildMessageReactionAddEvent event, String _GameKey, String _P1, String _P2, String _messageID) {
        this.event = event;
        this._GameKey = _GameKey;
        this._P1 = _P1;
        this._P2 = _P2;
        this._messageID = _messageID;
    }

    public GuildMessageReactionAddEvent getEvent() {
        return event;
    }

    public String get_GameKey() {
        return _GameKey;
    }

    public void set_GameKey(String _GameKey) {
        this._GameKey = _GameKey;
    }

    public String get_P1() {
        return _P1;
    }

    public void set_P1(String _P1) {
        this._P1 = _P1;
    }

    public String get_P2() {
        return _P2;
    }

    public void set_P2(String _P2) {
        this._P2 = _P2;
    }

    public String get_messageID() {
        return _messageID;
    }

    public void set_messageID(String _messageID) {
        this._messageID = _messageID;
    }
}
