package io.github.eoinkanro.telegram.product.chat.bot.core.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigPaths {

  CONFIG_FOLDER("conf"),
  CACHE_FOLDER("cache"),
  IMAGE_FOLDER("img"),
  MENU_FILE("menu.xml");

  private final String path;
}
