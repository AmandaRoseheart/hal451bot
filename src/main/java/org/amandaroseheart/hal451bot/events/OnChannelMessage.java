package org.amandaroseheart.hal451bot.events;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.amandaroseheart.hal451bot.commands.AdvisorCommand;

public class OnChannelMessage {

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public OnChannelMessage(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, event -> onChannelMessage(event));
    }

    public void onChannelMessage(ChannelMessageEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.startsWith("$should i") || message.startsWith("$should we")) {
            AdvisorCommand.execute(event);
        }
    }

}