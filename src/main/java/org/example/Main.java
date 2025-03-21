package org.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class Main {
    private static TelegramBot bot;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: tg-bot $BOT_TOKEN");
            return;
        }

        // Create your bot passing the token received from @BotFather
        bot = new TelegramBot(args[0]);
        // Register for updates
        bot.setUpdatesListener(updates -> {
            // ... process updates
            updates.forEach(update -> {processUpdate(update);});
            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        // Create Exception Handler
        }, e -> {
            if (e.response() != null) {
                // got bad response from telegram
                System.err.println("Got error response: " + e.response().errorCode() + ", " + e.response().description());
            } else {
                // probably network error
                e.printStackTrace();
            }
        });
        System.out.println("Listening for updates...");
    }

    private static void processUpdate(Update update) {
        if (update.message() != null) {
            System.out.println(update.message());
            // Private message or chat
            Long replyToId = update.message().from().id();
            if (update.message().chat() != null && update.message().chat().id() != null) {
                replyToId = update.message().chat().id();
            }
            // privet
            if (update.message().text() != null && update.message().text().equalsIgnoreCase("привет")) {
                replyTo(replyToId, "ты кто");
            }
            // anything else
            else {
                replyTo(replyToId, update.message().from().username() + ", иди в дупу");
            }

        }
    }

    private static SendResponse replyTo(Long chatId, String messageText) {
        return bot.execute(new SendMessage(chatId, messageText));
    }
}