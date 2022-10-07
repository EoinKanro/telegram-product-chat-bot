package io.github.eoinkanro.telegram.product.chat.bot.core.conf;

import io.github.eoinkanro.telegram.product.chat.bot.core.model.exception.ConfigException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

  @Bean
  public TelegramBotsApi telegramBotsApi() throws ConfigException {
    try {
      return new TelegramBotsApi(DefaultBotSession.class);
    } catch (Exception e) {
      throw new ConfigException("Error while starting bot");
    }
  }
}
