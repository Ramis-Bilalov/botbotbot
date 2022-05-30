package ru.bilalov.mywizard_bot;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

public class MyWizardTelegramBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    public MyWizardTelegramBot() {
        super();
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            try {
                Main main = new Main();
                main.setNumber(message.getText());
                List<Article> articleList = main.htmlParse();
                for (int i = 0; i < articleList.size(); i++) {
                    if (articleList.get(i).getNum().contains(message.getText())) {
                        sendMsg(message, articleList.get(i).toString());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
//            long chat_id = update.getMessage().getChatId();
//            try {
//                execute(new SendMessage(chat_id, "Hi " + update.getMessage().getText()));
//
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
        }
        return null;
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
//        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendApiMethod(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
