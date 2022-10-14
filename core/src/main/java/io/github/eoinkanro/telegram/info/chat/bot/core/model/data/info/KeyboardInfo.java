package io.github.eoinkanro.telegram.info.chat.bot.core.model.data.info;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Getter
@AllArgsConstructor
public class KeyboardInfo {

  private String name;
  private List<KeyboardRow> keyboard;

}
