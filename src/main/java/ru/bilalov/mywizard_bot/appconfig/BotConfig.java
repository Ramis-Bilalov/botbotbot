package ru.bilalov.mywizard_bot.appconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bilalov.mywizard_bot.MyWizardTelegramBot;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public MyWizardTelegramBot MyWizardTelegramBot() {
        MyWizardTelegramBot myWizardTelegramBot = new MyWizardTelegramBot();
        myWizardTelegramBot.setBotUserName(botUserName);
        myWizardTelegramBot.setBotToken(botToken);
        myWizardTelegramBot.setWebHookPath(webHookPath);

        return myWizardTelegramBot;
    }
}
