package org.amandaroseheart.hal451bot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdvisorCommand {

    private static List<String> yesNo = new ArrayList<>(List.of("Yes.", "No.", "Absolutely.", "No way!", "Definitely not.", "Nuh.", "Of course."));

    private static List<String> dontKnow = new ArrayList<>(List.of("I have no idea.", "How would I know?", "Drawing a blank here.", "Yus."));

    public static void execute(ChannelMessageEvent event) {

        if (event.getMessage().contains(" or ")) {
            String message = dontKnow.get(new Random().nextInt(dontKnow.size()));
            event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
        } else {
            String message = yesNo.get(new Random().nextInt(yesNo.size()));
            event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
        }

    }

}
