package org.amandaroseheart.hal451bot.persistence;

import org.amandaroseheart.hal451bot.commands.GuessTheClassicCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassicGamesDAO {

    private static List<String> classicGames = new ArrayList<>();

    static
    {
        readClassicGames();
    }

    public static String getRandomGame() {
        return classicGames
                .get(new Random().nextInt(classicGames.size()))
                .toLowerCase();
    }

    private static void readClassicGames() {
        InputStreamReader isr = new InputStreamReader(getFileAsInputStream("classic-games.txt"));
        BufferedReader br = new BufferedReader(isr);
        String line;
        try {
            while ((line = br.readLine()) != null) {
                classicGames.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static InputStream getFileAsInputStream(final String fileName) {
        return GuessTheClassicCommands.class.getClassLoader().getResourceAsStream(fileName);
    }



}
