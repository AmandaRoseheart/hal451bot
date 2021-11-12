package org.amandaroseheart.hal451bot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.amandaroseheart.hal451bot.persistence.ClassicGamesDAO;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GuessTheClassicCommand {

    private static String solution;

    private static Boolean activeGame = false;

    private static Set<Character> allCharacters;

    private static Set<Character> guessed;

    private static Set<Character> notGuessed;

    public static void startNewGame(final ChannelMessageEvent event) {
        if (!activeGame) {
            initNewGame();
            sendMessage(event, displayGameState());
        } else {
            sendMessage(event, "First finish the current game!");
        }
    }

    public static void guess(final ChannelMessageEvent event) {
        if (!activeGame) {
            sendMessage(event, "No active game!");
        } else {
            char guessed_char = event.getMessage().toLowerCase().charAt(7);
            guessed.add(guessed_char);
            updateNotGuessed(guessed_char);
            //todo if notGuessed = [] then you won
            sendMessage(event, displayGameState());
        }
    }

    public static void solution(final ChannelMessageEvent event) {
        if (!activeGame) {
            sendMessage(event, "No active game!");
        } else {
            String solution = event.getMessage().toLowerCase().substring(10).trim();
            if (solution.equals(GuessTheClassicCommand.solution.toLowerCase())) {
                endGame();
                sendMessage(event, "Correct! You win!");
            } else {
                sendMessage(event, String.format("Wrong answer!\n%s", displayGameState()));
            }
        }
    }

    private static void initNewGame() {
        solution = ClassicGamesDAO.getRandomGame();
        allCharacters = solution.replaceAll("[^1-9a-z]", "")
                .chars()
                .mapToObj(e -> (char) e).collect(Collectors.toSet());
        notGuessed = allCharacters;
        guessed = new HashSet<>();
        activeGame = true;
    }

    private static void endGame() {
        activeGame = false;
    }

    private static void updateNotGuessed(final char guessed_char) {
        if (allCharacters.contains(guessed_char)) notGuessed.remove(guessed_char);
    }

    private static String displayGameState() {
        String maskedSolution = solution;
        for (char c : notGuessed) {
            maskedSolution = maskedSolution.replaceAll(String.valueOf(c), "*");
        }
        return maskedSolution;
    }

    private static void sendMessage(final ChannelMessageEvent event, String message) {
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }

}
