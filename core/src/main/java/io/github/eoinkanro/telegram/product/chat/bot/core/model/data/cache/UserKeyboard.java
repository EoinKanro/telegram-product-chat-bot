package io.github.eoinkanro.telegram.product.chat.bot.core.model.data.cache;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserKeyboard implements Serializable {

  private String chatId;
  private String keyboardLink;

}
