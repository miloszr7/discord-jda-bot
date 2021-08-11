package games;

import config.DatabaseConnection;
import models.StreamPlayers;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TicTacToe extends ListenerAdapter {

    // TODO: gameBoard variable may be the same for everyone so assign it after receiving $ttt command

    private HashMap<String, String> GameLobby = new HashMap<>();

    private boolean PlayerOneTurn = true;
    private boolean PlayerTwoTurn = false;

    private String[][] gameBoard = {
            {":new_moon:", ":new_moon: ", ":new_moon: "},
            {":new_moon:", ":new_moon: ", ":new_moon: "},
            {":new_moon:", ":new_moon: ", ":new_moon: "}
    };

    private void ClearGame() {
        gameBoard = new String[][]{
                {":new_moon:", ":new_moon: ", ":new_moon: "},
                {":new_moon:", ":new_moon: ", ":new_moon: "},
                {":new_moon:", ":new_moon: ", ":new_moon: "}
        };
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean AllowedMove(String[][] gameBoard, GuildMessageReactionAddEvent event) {

        if (gameBoard[0][0].equals(":x:") && PlayerTwoTurn) {
            if (event.getReactionEmote().getEmoji().equals("1\uFE0F\u20E3")) {
                return false;
            }
        }
        if (gameBoard[0][0].equals(":o:") && PlayerOneTurn) {
            if (event.getReactionEmote().getEmoji().equals("1\uFE0F\u20E3")) {
                return false;
            }
        }

        if (gameBoard[0][1].equals(":x:") && PlayerTwoTurn) {
            if (event.getReactionEmote().getEmoji().equals("2\uFE0F\u20E3")) {
                return false;
            }
        }
        if (gameBoard[0][1].equals(":o:") && PlayerOneTurn) {
            if (event.getReactionEmote().getEmoji().equals("2\uFE0F\u20E3")) {
                return false;
            }
        }

        if (gameBoard[0][2].equals(":x:") && PlayerTwoTurn) {
            if (event.getReactionEmote().getEmoji().equals("3\uFE0F\u20E3")) {
                return false;
            }
        }
        if (gameBoard[0][2].equals(":o:") && PlayerOneTurn) {
            if (event.getReactionEmote().getEmoji().equals("3\uFE0F\u20E3")) {
                return false;
            }
        }

        if (gameBoard[1][0].equals(":x:") && PlayerTwoTurn) {
            if (event.getReactionEmote().getEmoji().equals("4\uFE0F\u20E3")) {
                return false;
            }
        }
        if (gameBoard[1][0].equals(":o:") && PlayerOneTurn) {
            if (event.getReactionEmote().getEmoji().equals("4\uFE0F\u20E3")) {
                return false;
            }
        }

        if (gameBoard[1][1].equals(":x:") && PlayerTwoTurn) {
            if (event.getReactionEmote().getEmoji().equals("5\uFE0F\u20E3")) {
                return false;
            }
        }
        if (gameBoard[1][1].equals(":o:") && PlayerOneTurn) {
            if (event.getReactionEmote().getEmoji().equals("5\uFE0F\u20E3")) {
                return false;
            }
        }

        if (gameBoard[1][2].equals(":x:") && PlayerTwoTurn) {
            if (event.getReactionEmote().getEmoji().equals("6\uFE0F\u20E3")) {
                return false;
            }
        }
        if (gameBoard[1][2].equals(":o:") && PlayerOneTurn) {
            if (event.getReactionEmote().getEmoji().equals("6\uFE0F\u20E3")) {
                return false;
            }
        }

        if (gameBoard[2][0].equals(":x:") && PlayerTwoTurn) {
            if (event.getReactionEmote().getEmoji().equals("7\uFE0F\u20E3")) {
                return false;
            }
        }
        if (gameBoard[2][0].equals(":o:") && PlayerOneTurn) {
            if (event.getReactionEmote().getEmoji().equals("7\uFE0F\u20E3")) {
                return false;
            }
        }

        if (gameBoard[2][1].equals(":x:") && PlayerTwoTurn) {
            if (event.getReactionEmote().getEmoji().equals("8\uFE0F\u20E3")) {
                return false;
            }
        }
        if (gameBoard[2][1].equals(":o:") && PlayerOneTurn) {
            if (event.getReactionEmote().getEmoji().equals("8\uFE0F\u20E3")) {
                return false;
            }
        }

        if (gameBoard[2][2].equals(":x:") && PlayerTwoTurn) {
            if (event.getReactionEmote().getEmoji().equals("9\uFE0F\u20E3")) {
                return false;
            }
        }
        if (gameBoard[2][2].equals(":o:") && PlayerOneTurn) {
            if (event.getReactionEmote().getEmoji().equals("9\uFE0F\u20E3")) {
                return false;
            }
        }

        return true;
    }

    private void WinMove(GuildMessageReactionAddEvent event, String[][] gameBoard, String Player1, String Player2, String messageID, String gameKey) throws Exception {

        String P1Win = "<@" + Player1 + "> is a winner!";
        String P2Win = "<@" + Player2 + "> is a winner!";

        if (gameBoard[0][0].equals(":x:") && gameBoard[0][1].equals(":x:") && gameBoard[0][2].equals(":x:") ||
                gameBoard[1][0].equals(":x:") && gameBoard[1][1].equals(":x:") && gameBoard[1][2].equals(":x:") ||
                gameBoard[2][0].equals(":x:") && gameBoard[2][1].equals(":x:") && gameBoard[2][2].equals(":x:") ||
                gameBoard[0][0].equals(":x:") && gameBoard[1][0].equals(":x:") && gameBoard[2][0].equals(":x:") ||
                gameBoard[0][1].equals(":x:") && gameBoard[1][1].equals(":x:") && gameBoard[2][1].equals(":x:") ||
                gameBoard[0][2].equals(":x:") && gameBoard[1][2].equals(":x:") && gameBoard[2][2].equals(":x:") ||
                gameBoard[0][0].equals(":x:") && gameBoard[1][1].equals(":x:") && gameBoard[2][2].equals(":x:") ||
                gameBoard[0][2].equals(":x:") && gameBoard[1][1].equals(":x:") && gameBoard[2][0].equals(":x:")) {

            ClearGame();

            DatabaseConnection conn = new DatabaseConnection();

            conn.query("DELETE FROM game WHERE key = "+gameKey+"");

            for (String x : GameLobby.keySet()) {
                for (String y : GameLobby.values()) {
                    if (x.contains(Player1) || y.contains(Player2)) {
                        GameLobby.remove(Player1, Player2);
                    }
                }
            }

            event.getChannel().sendMessage(P1Win).queue();
            event.getChannel().deleteMessageById(messageID).queue();

        } else if (gameBoard[0][0].equals(":x:") && gameBoard[0][1].equals(":x:") && gameBoard[0][2].equals(":o:") ||
                gameBoard[1][0].equals(":x:") && gameBoard[1][1].equals(":x:") && gameBoard[1][2].equals(":o:") ||
                gameBoard[2][0].equals(":x:") && gameBoard[2][1].equals(":x:") && gameBoard[2][2].equals(":o:") ||
                gameBoard[0][0].equals(":x:") && gameBoard[1][0].equals(":x:") && gameBoard[2][0].equals(":o:") ||
                gameBoard[0][1].equals(":x:") && gameBoard[1][1].equals(":x:") && gameBoard[2][1].equals(":o:") ||
                gameBoard[0][2].equals(":x:") && gameBoard[1][2].equals(":x:") && gameBoard[2][2].equals(":o:") ||
                gameBoard[0][0].equals(":x:") && gameBoard[1][1].equals(":x:") && gameBoard[2][2].equals(":o:") ||
                gameBoard[0][2].equals(":x:") && gameBoard[1][1].equals(":x:") && gameBoard[2][0].equals(":o:")) {

            ClearGame();

            DatabaseConnection conn = new DatabaseConnection();

            conn.query("DELETE FROM game WHERE key = "+gameKey+"");

            for (String x : GameLobby.keySet()) {
                for (String y : GameLobby.values()) {
                    if (x.contains(Player1) || y.contains(Player2)) {
                        GameLobby.remove(Player1, Player2);
                    }
                }
            }

            event.getChannel().sendMessage(P2Win).queue();
            event.getChannel().deleteMessageById(messageID).queue();

        }

    }

    private int count_move = 0;

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {

        try {

            if (!event.getMember().getId().equals("740914695501774858")) {

                StreamPlayers players = new StreamPlayers();

                    String url = "jdbc:sqlite:database.db";
                    Connection connection = DriverManager.getConnection(url);

                    Statement st = connection.createStatement();

                    String SQL = "SELECT * FROM game WHERE PlayerOne = " + event.getMember().getId() + " OR PlayerTwo = " + event.getMember().getId() + "";

                    ResultSet resultSet = st.executeQuery(SQL);

                    System.out.println("Searching P1 or P2: " + event.getMember().getId());

                    String P2 = "";
                    String P1 = "";

                    while (resultSet.next()) {
                        P2 = resultSet.getString("PlayerTwo");
                        P1 = resultSet.getString("PlayerOne");
                    }

                    System.out.println("Found P1 and P2: " + P1 + " " + P2);

                    String _check = "SELECT * FROM game WHERE PlayerOne = " + P1 + " AND PlayerTwo = " + P2 + "";

                    ResultSet resultSet1 = st.executeQuery(_check);

                    // set variables for method
                    while (resultSet1.next()) {
                        players.set_GameKey(resultSet1.getString("key"));
                        players.set_P1(resultSet1.getString("PlayerOne"));
                        players.set_P2(resultSet1.getString("PlayerTwo"));
                        players.set_messageID(resultSet1.getString("messageID"));
                    }

                    System.out.println("Information about players: \n" + players.get_P1() + " vs " + players.get_P2()
                            + " key: " + players.get_GameKey() + " messageID: " + players.get_messageID());

                if (count_move == 9) {

                    event.getChannel().sendMessage("<@" + players.get_P1() + "> vs <@" + players.get_P2() + ">" + "\n :sparkles: **draw** :sparkles:").queue();

                    ClearGame();

                    DatabaseConnection conn = new DatabaseConnection();

                    conn.query("DELETE FROM game WHERE key = "+players.get_GameKey()+"");

                    for (String x : GameLobby.keySet()) {
                        for (String y : GameLobby.values()) {
                            if (x.contains(players.get_P1()) || y.contains(players.get_P2())) {
                                GameLobby.remove(players.get_P1(), players.get_P2());
                            }
                            System.out.println("\n" + x + " " + y);
                        }
                    }


                    event.getChannel().deleteMessageById(players.get_messageID()).queue();

                }

                // game mechanics
                if (AllowedMove(gameBoard, event)) {

                    if (PlayerOneTurn) {

                        if (event.getReactionEmote().getEmoji().equals("1\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P1())) {

                            count_move++;

                            gameBoard[0][0] = ":x:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = false;
                            PlayerTwoTurn = true;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("2\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P1())) {

                            count_move++;

                            gameBoard[0][1] = ":x:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = false;
                            PlayerTwoTurn = true;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("3\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P1())) {

                            count_move++;

                            gameBoard[0][2] = ":x:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = false;
                            PlayerTwoTurn = true;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("4\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P1())) {

                            count_move++;

                            gameBoard[1][0] = ":x:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = false;
                            PlayerTwoTurn = true;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("5\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P1())) {

                            count_move++;

                            gameBoard[1][1] = ":x:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = false;
                            PlayerTwoTurn = true;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("6\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P1())) {

                            count_move++;

                            gameBoard[1][2] = ":x:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = false;
                            PlayerTwoTurn = true;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("7\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P1())) {

                            count_move++;

                            gameBoard[2][0] = ":x:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = false;
                            PlayerTwoTurn = true;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("8\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P1())) {

                            count_move++;

                            gameBoard[2][1] = ":x:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = false;
                            PlayerTwoTurn = true;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("9\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P1())) {

                            count_move++;

                            gameBoard[2][2] = ":x:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = false;
                            PlayerTwoTurn = true;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                    } else if (PlayerTwoTurn) {

                        if (event.getReactionEmote().getEmoji().equals("1\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P2())) {

                            count_move++;

                            gameBoard[0][0] = ":o:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = true;
                            PlayerTwoTurn = false;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("2\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P2())) {

                            count_move++;

                            gameBoard[0][1] = ":o:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = true;
                            PlayerTwoTurn = false;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("3\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P2())) {

                            count_move++;

                            gameBoard[0][2] = ":o:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = true;
                            PlayerTwoTurn = false;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("4\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P2())) {

                            count_move++;

                            gameBoard[1][0] = ":o:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = true;
                            PlayerTwoTurn = false;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("5\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P2())) {

                            count_move++;

                            gameBoard[1][1] = ":o:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = true;
                            PlayerTwoTurn = false;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("6\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P2())) {

                            count_move++;

                            gameBoard[1][2] = ":o:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = true;
                            PlayerTwoTurn = false;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("7\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P2())) {

                            count_move++;

                            gameBoard[2][0] = ":o:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = true;
                            PlayerTwoTurn = false;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("8\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P2())) {

                            count_move++;

                            gameBoard[2][1] = ":o:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = true;
                            PlayerTwoTurn = false;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }

                        if (event.getReactionEmote().getEmoji().equals("9\uFE0F\u20E3") && event.getMember().getId().equals(players.get_P2())) {

                            count_move++;

                            gameBoard[2][2] = ":o:";

                            event.getChannel().editMessageById(players.get_messageID(), PrintGameBoard(gameBoard)).queue();

                            PlayerOneTurn = true;
                            PlayerTwoTurn = false;

                            WinMove(event, gameBoard, players.get_P1(), players.get_P2(), players.get_messageID(), players.get_GameKey());

                        }
                    }

                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private String PrintGameBoard(String[][] gameBoard) {
        String content = "";
        for (String[] row : gameBoard) {
            for (String s : row) {
                content += s;
            }
            content += "\n";
        }
        return content;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (GameLobby.containsKey(event.getAuthor().getId()) || GameLobby.containsValue(event.getAuthor().getId())) {

            event.getMessage().delete().queue();

            event.getChannel().sendMessage(":name_badge: `You are already in the game`").queue();

        } else {

            if (command.length == 2 && command[0].equalsIgnoreCase("$ttt")) {

                Member mentioned = event.getMessage().getMentionedMembers().get(0);

                event.getChannel().sendMessage("> **Starting game**...").complete().delete().queueAfter(4, TimeUnit.SECONDS);
                event.getChannel().sendTyping().queue();

                event.getChannel().sendMessage(PrintGameBoard(gameBoard)).queueAfter(2, TimeUnit.SECONDS, message -> {

                    String messageID = "";

                    for (int i = 1; i <= 9; i++) {
                        message.addReaction(i + "\uFE0F\u20E3").queue();
                    }

                    messageID = message.getId();

                    GameLobby.put(event.getAuthor().getId(), mentioned.getId());

                    try {

                        int r = (int) (Math.random() * 9999) + 1;

                        DatabaseConnection connection = new DatabaseConnection();

                        connection.query("INSERT INTO game VALUES("+r+","+event.getAuthor().getId()+","+mentioned.getId()+","+messageID+")");

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                });

            }
        }

    }
}
