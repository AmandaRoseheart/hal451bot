package org.amandaroseheart.hal451bot.features;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public class ChannelNotificationOnChannelMessage {

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public ChannelNotificationOnChannelMessage(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(ChannelMessageEvent.class, event -> onDonation(event));
    }

    /**
     * Subscribe to the Donation Event
     */
    public void onDonation(ChannelMessageEvent event) {
        if (event.getMessage().startsWith("$test")) {
            event.getTwitchChat().sendMessage(event.getChannel().getName(), "Hello " + event.getUser().getName() + "!");
        }
    }

}