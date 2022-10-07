package io.github.eoinkanro.telegram.product.chat.bot.core.conf;

import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu.Answer;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.ConfigPaths;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu.Keyboard;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu.Menu;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu.MenuMainBlock;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu.Row;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.exception.ConfigException;
import io.github.eoinkanro.telegram.product.chat.bot.core.utils.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class MenuConfig {

  public static final String START = "/start";

  @Autowired
  private FileUtils fileUtils;

  private final Map<String, String> answers = new HashMap<>();
  private final Map<String, List<KeyboardRow>> keyboards = new LinkedHashMap<>();
  private final Map<String, String> images = new HashMap<>();

  @PostConstruct
  private void init() throws ConfigException {
    File menuFile = fileUtils.getConfFile(ConfigPaths.MENU_FILE.getPath());
    Menu menu = unmarshallMenu(menuFile);
    if (menu != null) {
      checkMenu(menu);
      initMenu(menu);
    }
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
    checkLinks(links, menu.getKeyboards());
    checkLinks(links, menu.getAnswers());
  }

  private void checkLinks(List<String> existedLinks, List<? extends MenuMainBlock> menuMainBlocks)
      throws ConfigException {
    for (MenuMainBlock menuMainBlock : menuMainBlocks) {
      if (menuMainBlock.getLink() == null || menuMainBlock.getLink().isEmpty()) {
        throw new ConfigException(String.format("Link of %s couldn't be empty", menuMainBlock.getClass().getName()));
      }else if (existedLinks.contains(menuMainBlock.getLink())) {
        throw new ConfigException("Duplicated link: " + menuMainBlock.getLink());
      }
      existedLinks.add(menuMainBlock.getLink());
    }
  }


  private void initMenu(Menu menu) {
    initAnswers(menu.getAnswers());
    initKeyboards(menu.getKeyboards());
  }

  private void initAnswers(List<Answer> answersToInit) {
    for (Answer answer : answersToInit) {
      answers.put(answer.getLink(), answer.getText());

      if (answer.getImg() != null && !answer.getImg().isEmpty()) {
        images.put(answer.getLink(), answer.getImg());
      }
    }
  }

  private void initKeyboards(List<Keyboard> keyboardsToInit) {
    for (Keyboard keyboard : keyboardsToInit) {
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

  @Nullable
  public String getAnswer(String link) {
    return answers.get(link);
  }

  @Nullable
  public List<KeyboardRow> getKeyboard(String link) {
    return keyboards.get(link);
  }

  @Nullable
  public List<KeyboardRow> getFirstKeyboard() {
    if (!keyboards.isEmpty()) {
      return keyboards.entrySet().iterator().next().getValue();
    }
    return null;
  }

  @Nullable
  public String getImage(String link) {
    return images.get(link);
  }

}
