package io.github.eoinkanro.telegram.product.chat.bot.core.conf;

import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu.Answer;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.ConfigPaths;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu.Keyboard;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu.Menu;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu.Row;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.exception.ConfigException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class MenuConfig {

  public static final String START = "/start";

  private final Map<String, String> answers = new HashMap<>();
  private final Map<String, List<KeyboardRow>> keyboards = new LinkedHashMap<>();

  @PostConstruct
  private void init() throws ConfigException {
    File menuFile = loadConf();
    Menu menu = unmarshallMenu(menuFile);
    if (menu != null) {
      checkMenu(menu);
      initMenu(menu);
    }
  }

  private File loadConf() {
    String menuFilePathBuilder = System.getProperty("user.dir")
        + File.separator
        + ConfigPaths.CONFIG_PATH.getPath()
        + File.separator
        + ConfigPaths.MENU_FILE_NAME.getPath();
    return new File(menuFilePathBuilder);
  }

  private Menu unmarshallMenu(File menuFile) throws ConfigException {
    if (menuFile.exists()) {
      try {
        JAXBContext context = JAXBContext.newInstance(Menu.class);
        return  (Menu) context.createUnmarshaller().unmarshal(menuFile);
      } catch (Exception e) {
        throw new ConfigException("Error while loading " + menuFile.getAbsolutePath(), e);
      }
    }
    return null;
  }

  private void checkMenu(Menu menu) throws ConfigException {
    List<String> links = new ArrayList<>();

    for (Keyboard keyboard : menu.getKeyboards()) {
      if (keyboard.getLink() == null || keyboard.getLink().isEmpty()) {
        throw new ConfigException("Link of keyboard couldn't be empty");
      }else if (links.contains(keyboard.getLink())) {
        throw new ConfigException("Duplicated link: " + keyboard.getLink());
      }
      links.add(keyboard.getLink());
    }

    for (Answer answer : menu.getAnswers()) {
      if (answer.getLink() == null || answer.getLink().isEmpty()) {
        throw new ConfigException("Link of answer couldn't be empty");
      } else if (links.contains(answer.getLink())) {
        throw new ConfigException("Duplicated link: " + answer.getLink());
      }
      links.add(answer.getLink());
    }
  }

  private void initMenu(Menu menu) {
    for (Answer answer : menu.getAnswers()) {
      answers.put(answer.getLink(), answer.getText());
    }

    for (Keyboard keyboard : menu.getKeyboards()) {
      String keyboardLink = keyboard.getLink();
      List<KeyboardRow> telegramKeyboard = new ArrayList<>();

      for (Row row : keyboard.getRowList()) {
        KeyboardRow keyboardRow = new KeyboardRow();
        row.getButtonList().forEach(button ->
            keyboardRow.add(new KeyboardButton(button.getText())));
        if (!keyboardRow.isEmpty()) {
          telegramKeyboard.add(keyboardRow);
        }
      }

      if (!telegramKeyboard.isEmpty()) {
        keyboards.put(keyboardLink, telegramKeyboard);
      }
    }
  }

  public boolean checkAnswer(String link) {
    return answers.containsKey(link);
  }

  public boolean checkKeyboard(String link) {
    return keyboards.containsKey(link);
  }

  public String getAnswer(String link) {
    return answers.get(link);
  }

  public List<KeyboardRow> getKeyboard(String link) {
    return keyboards.get(link);
  }

  public List<KeyboardRow> getFirstKeyboard() {
    return keyboards.entrySet().iterator().next().getValue();
  }

}
