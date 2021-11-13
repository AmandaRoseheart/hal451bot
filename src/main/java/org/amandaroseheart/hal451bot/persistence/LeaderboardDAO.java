package org.amandaroseheart.hal451bot.persistence;

import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardDAO {

    private static Map<String, Integer> scores = new HashMap<>();

    public static void updateLeaderboard(final String username) {
        if (!scores.containsKey(username)) {
            scores.put(username, 1);
        } else {
            int score = scores.get(username);
            scores.remove(username);
            scores.put(username, score + 1);
        }
    }

    public static String showStandings() {
        return sortedScores().toString();
    }

    public static LinkedHashMap<String, Integer> sortedScores() {
        return scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

}