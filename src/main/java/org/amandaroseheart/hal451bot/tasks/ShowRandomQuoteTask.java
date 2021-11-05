package org.amandaroseheart.hal451bot.tasks;

import com.github.twitch4j.TwitchClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class ShowRandomQuoteTask extends TimerTask {

    TwitchClient twitchClient;

    List<String> quotes = new ArrayList<>();

    public ShowRandomQuoteTask(TwitchClient client) {
        twitchClient = client;
        try {
            readQuotes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        twitchClient.getChat().sendMessage("HAL451", getRandomLine());
    }

    private String getRandomLine() {
        return quotes
                .get(new Random().nextInt(quotes.size()))
                .replace("Quote", "");
    }

    private void readQuotes() throws IOException {
        InputStreamReader isr = new InputStreamReader(getFileAsInputStream("quotes.txt"));
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            quotes.add(line);
        }
    }

    private InputStream getFileAsInputStream(final String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }

}