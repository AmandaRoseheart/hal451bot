package org.amandaroseheart.hal451bot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public class GuessTheClassicCommands {

    private static GuessTheClassicGame game;

    public static void startNewGame(final ChannelMessageEvent event) {
        if (noCurrentGame()) {
            game = new GuessTheClassicGame();
            game.startNewGame(event);
        } else {
            sendMessage(event, "First finish the current game!");
        }
    }

    public static void guess(final ChannelMessageEvent event) {
        if (noCurrentGame()) {
            sendMessage(event, "No active game!");
        } else {
            game.guess(event);
        }
    }

    public static void solution(final ChannelMessageEvent event) {
        if (noCurrentGame()) {
            sendMessage(event, "No active game!");
        } else {
            boolean completed = game.solution(event);
            if (completed) game = null;
        }
    }

    private static Boolean noCurrentGame() {
        return game == null;
    }

    private static void sendMessage(final ChannelMessageEvent event, String message) {
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }

}
