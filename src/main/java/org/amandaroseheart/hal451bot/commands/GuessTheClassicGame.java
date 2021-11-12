package org.amandaroseheart.hal451bot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.amandaroseheart.hal451bot.persistence.ClassicGamesDAO;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GuessTheClassicGame {

    private String solution;

    private Set<Character> allCharacters;

    private Set<Character> guessed;

    private Set<Character> notGuessed;

    public GuessTheClassicGame() {
    }

    public void startNewGame(final ChannelMessageEvent event) {
        initNewGame();
        sendMessage(event, displayGameState());
    }

    public void guess(final ChannelMessageEvent event) {
        char guessed_char = event.getMessage().toLowerCase().charAt(7);
        guessed.add(guessed_char);
        updateNotGuessed(guessed_char);
        sendMessage(event, displayGameState());
    }

    public Boolean solution(final ChannelMessageEvent event) {
        String answer = event.getMessage().toLowerCase().substring(10).trim();
        if (answer.equals(solution.toLowerCase())) {
            sendMessage(event, "Correct! You win!");
            return true;
        } else {
            sendMessage(event, String.format("Wrong answer!\n%s", displayGameState()));
            return false;
        }
    }

    private void initNewGame() {
        solution = ClassicGamesDAO.getRandomGame().toLowerCase();
        allCharacters = solution.replaceAll("[^1-9a-z]", "")
                .chars()
                .mapToObj(e -> (char) e).collect(Collectors.toSet());
        notGuessed = allCharacters;
        guessed = new HashSet<>();
    }

    private void updateNotGuessed(final char guessed_char) {
        if (allCharacters.contains(guessed_char)) notGuessed.remove(guessed_char);
    }

    private String displayGameState() {
        String maskedSolution = solution;
        for (char c : notGuessed) {
            maskedSolution = maskedSolution.replaceAll(String.valueOf(c), "*");
        }
        return maskedSolution;
    }

    private void sendMessage(final ChannelMessageEvent event, String message) {
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }

}
