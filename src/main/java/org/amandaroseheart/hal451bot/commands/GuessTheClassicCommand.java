package org.amandaroseheart.hal451bot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.amandaroseheart.hal451bot.persistence.ClassicGamesDAO;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GuessTheClassicCommand {

    private static String solution;

    private static final Integer maxLives = 5;

    private static Integer lives;

    private static Boolean activeGame = false;

    private static Set<Character> allCharacters;

    private static Set<Character> guessed;

    private static Set<Character> notGuessed;

    public static void startNewGame(ChannelMessageEvent event) {
        if (!activeGame) {
            initNewGame();
            sendMessage(event, constructMessage());
        } else {
            sendMessage(event, "First finish the current game!");
        }
    }

    public static void guess(ChannelMessageEvent event) {
        if (!activeGame) {
            sendMessage(event, "No active game!");
        } else {
            char guessed_char = event.getMessage().toLowerCase().charAt(7);
            updateLives(guessed_char);
            if (lives == 0) {
                endGame();
                sendMessage(event, "No lives left, game over!\n" + "The game was " + solution + ".");
            } else {
                guessed.add(guessed_char);
                updateNotGuessed(guessed_char);
                //todo if notGuessed = [] then you won
                sendMessage(event, constructMessage());
            }
        }
    }

    public static void solution(ChannelMessageEvent event) {
        if (!activeGame) {
            sendMessage(event, "No active game!");
        } else {
            String solution = event.getMessage().toLowerCase().substring(10).trim();
            if (solution.equals(GuessTheClassicCommand.solution.toLowerCase())) {
                endGame();
                sendMessage(event, "Correct! You win!");
            } else {
                lives -= 1;
                if (lives == 0) {
                    endGame();
                    sendMessage(event, "Wrong answer. No lives left, game over!\n" + "The game was " + GuessTheClassicCommand.solution + ".");
                } else {
                    sendMessage(event, "Wrong answer!\n" + constructMessage());
                }
            }
        }
    }

    private static void initNewGame() {
        solution = ClassicGamesDAO.getRandomGame();
        System.out.println(solution);
        allCharacters = solution
                .replaceAll("[^1-9a-z]", "")
                .chars()
                .mapToObj(e -> (char) e).collect(Collectors.toSet());
        notGuessed = allCharacters;
        guessed = new HashSet<>();
        activeGame = true;
        lives = maxLives;
    }

    private static void endGame() {
        activeGame = false;
    }

    private static void updateLives(char guessed_char) {
        if (!allCharacters.contains(guessed_char)) lives -= 1;
    }

    private static void updateNotGuessed(char guessed_char) {
        if (allCharacters.contains(guessed_char)) notGuessed.remove(guessed_char);
    }

    private static String constructMessage() {
        return String.format("%s lives: %s/%s", maskSolution(), lives, maxLives);
    }

    private static String maskSolution() {
        String maskedSolution = solution;
        for (char c : notGuessed) {
            maskedSolution = maskedSolution.replaceAll(String.valueOf(c), "*");
        }
        return maskedSolution;
    }

    private static void sendMessage(ChannelMessageEvent event, String message) {
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }

}
