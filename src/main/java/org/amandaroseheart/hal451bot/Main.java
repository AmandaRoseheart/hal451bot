package org.amandaroseheart.hal451bot;

public class Main {

    public static void main(String[] args) {
        HAL451bot bot = new HAL451bot();
        bot.registerFeatures();
        bot.registerTasks();
        bot.start();
    }

}