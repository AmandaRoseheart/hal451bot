package org.amandaroseheart.hal451bot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.amandaroseheart.hal451bot.events.OnChannelMessage;
import org.amandaroseheart.hal451bot.tasks.ShowRandomQuoteTask;

import java.util.Timer;
import java.util.TimerTask;

public class HAL451bot {

    private TwitchClient twitchClient;

    private String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");

    private String CHANNEL_NAME = System.getenv("CHANNEL_NAME");

    public HAL451bot() {
        OAuth2Credential credential = new OAuth2Credential("twitch", ACCESS_TOKEN);
        twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withChatAccount(credential)
                .build();
    }

    public void registerFeatures() {
        SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);
        // Register Event-based features
        OnChannelMessage onChannelMessage = new OnChannelMessage(eventHandler);
    }

    public void registerTasks() {
        TimerTask showRandomQuoteTask = new ShowRandomQuoteTask(twitchClient);
        new Timer().scheduleAtFixedRate(showRandomQuoteTask, 0,  1800000); // every 30 minutes
    }

    public void start() {
        twitchClient.getChat().joinChannel(CHANNEL_NAME);
    }

}