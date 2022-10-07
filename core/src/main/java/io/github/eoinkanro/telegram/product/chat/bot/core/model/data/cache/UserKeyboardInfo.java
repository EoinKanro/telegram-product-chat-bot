package io.github.eoinkanro.telegram.product.chat.bot.core.model.data.cache;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserKeyboardInfo implements Serializable {

  private String chatId;
  private String currentKeyboard;
  private Deque<String> previousKeyboards = new LinkedList<>();

}
