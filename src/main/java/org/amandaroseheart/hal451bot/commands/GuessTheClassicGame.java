package org.amandaroseheart.hal451bot.commands;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.amandaroseheart.hal451bot.persistence.ClassicGamesDAO;
import org.amandaroseheart.hal451bot.persistence.LeaderboardDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuessTheClassicGame {

    private String solution;

    private Set<Character> allCharacters;

    private List<Character> wrongAnswers;

    private Set<Character> notGuessed;

    public GuessTheClassicGame() {
    }

    public void startNewGame(final ChannelMessageEvent event) {
        initNewGame();
        sendMessage(event, displayGameState());
    }

    public synchronized void guess(final ChannelMessageEvent event) {
        char guessed_char = event.getMessage().toLowerCase().charAt(7);
        updateWrongAnswers(guessed_char);
        updateNotGuessed(guessed_char);
        sendMessage(event, displayGameState());
    }

    /**
     * Returns true if answer is correct, false otherwise.
     */
    public synchronized Boolean solution(final ChannelMessageEvent event) {
        String answer = event.getMessage().toLowerCase().substring(10).trim();
        if (answer.equals(solution.toLowerCase())) {
            sendMessage(event, "Correct! You win!");
            LeaderboardDAO.updateLeaderboard(event.getUser().getName(), notGuessed.size());
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
        wrongAnswers = new ArrayList<>();
    }

    private void updateNotGuessed(final char guessed_char) {
        if (allCharacters.contains(guessed_char)) notGuessed.remove(guessed_char);
    }

    private void updateWrongAnswers(final char guessed_char) {
        if (!allCharacters.contains(guessed_char)) wrongAnswers.add(guessed_char);
    }

    private String displayGameState() {
        return String.format("%s %s", displayMaskedSolution(), displayWrongAnswers());
    }

    private String displayWrongAnswers() {
        return wrongAnswers.toString().replaceAll(", ", "");
    }

    private String displayMaskedSolution() {
        String maskedSolution = solution;
        for (char c : notGuessed) {
            maskedSolution = maskedSolution.replaceAll(String.valueOf(c), "\u2022");
        }
        return maskedSolution;
    }

    private void sendMessage(final ChannelMessageEvent event, String message) {
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }

}