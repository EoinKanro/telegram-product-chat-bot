package io.github.eoinkanro.telegram.product.chat.bot.core.conf;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Getter
@Configuration
public class BotConfig {

  @Bean
  public SetWebhook setWebhookBean(BotSettings botSettings) {
    return SetWebhook.builder().url(botSettings.getWebhookPath()).build();
  }

}
