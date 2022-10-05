package io.github.eoinkanro.telegram.product.chat.bot.core.model.exception;

public class ConfigException extends Exception {

  public ConfigException(String message) {
    super(message);
  }

  public ConfigException(String message, Throwable cause) {
    super(message, cause);
  }
}
