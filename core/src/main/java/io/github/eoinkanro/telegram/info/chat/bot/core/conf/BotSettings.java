package io.github.eoinkanro.telegram.info.chat.bot.core.conf;

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

  @Value(value = "${telegram.bot.defaultAnswer:Choose element in menu}")
  private String defaultAnswer;

  @Value(value = "${telegram.bot.backButton:Back}")
  private String backButton;

}