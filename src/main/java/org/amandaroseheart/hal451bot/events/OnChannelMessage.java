package org.amandaroseheart.hal451bot.events;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.amandaroseheart.hal451bot.commands.AdvisorCommand;
import org.amandaroseheart.hal451bot.commands.GuessTheClassicCommands;

public class OnChannelMessage {

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public OnChannelMessage(final SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, event -> onChannelMessage(event));
    }

    public void onChannelMessage(final ChannelMessageEvent event) {
        final String message = event.getMessage().toLowerCase();
        if (message.startsWith("!should")) {
            AdvisorCommand.execute(event);
        }
        if (message.startsWith("!start-guess-the-classic")) {
            GuessTheClassicCommands.startNewGame(event);
        }
        if (message.startsWith("!guess")) {
            GuessTheClassicCommands.guess(event);
        }
        if (message.startsWith("!solution")) {
            GuessTheClassicCommands.solution(event);
        }
        if (message.startsWith("!leaderboard")) {
            GuessTheClassicCommands.leaderboard(event);
        }
    }

}