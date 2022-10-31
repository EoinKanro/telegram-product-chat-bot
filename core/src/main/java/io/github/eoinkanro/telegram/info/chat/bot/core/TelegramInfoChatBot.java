package io.github.eoinkanro.telegram.info.chat.bot.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.github.eoinkanro.telegram.info.chat.bot")
public class TelegramInfoChatBot {

  public static void main(String[] args) {
    SpringApplication.run(TelegramInfoChatBot.class);
  }
}