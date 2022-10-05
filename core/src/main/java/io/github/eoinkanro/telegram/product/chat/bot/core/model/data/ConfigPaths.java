package io.github.eoinkanro.telegram.product.chat.bot.core.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigPaths {

  CONFIG_PATH("conf"),
  MENU_FILE_NAME("menu.xml"),
  CACHE("cache");

  private final String path;
}
