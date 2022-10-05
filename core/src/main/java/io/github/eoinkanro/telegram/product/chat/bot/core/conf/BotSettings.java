package io.github.eoinkanro.telegram.product.chat.bot.core.conf;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BotSettings {

  @Value("${telegram.bot.username}")
  private String username;

  @Value("${telegram.bot.token}")
  private String token;

  @Value("${telegram.bot.webhookPath}")
  private String webhookPath;

  @Value(value = "${telegram.bot.defaultAnswer:Menu}")
  private String defaultAnswer;

}
