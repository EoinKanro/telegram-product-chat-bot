package io.github.eoinkanro.telegram.info.chat.bot.core.api;

import io.github.eoinkanro.telegram.info.chat.bot.core.conf.BotSettings;
import io.github.eoinkanro.telegram.info.chat.bot.core.conf.MenuConfig;
import io.github.eoinkanro.telegram.info.chat.bot.core.conf.OpenNLPConf;
import io.github.eoinkanro.telegram.info.chat.bot.core.model.data.cache.UserKeyboardInfo;
import io.github.eoinkanro.telegram.info.chat.bot.core.model.data.info.KeyboardInfo;
import io.github.eoinkanro.telegram.info.chat.bot.core.service.UserKeyboardService;
import io.github.eoinkanro.telegram.info.chat.bot.core.utils.FileUtils;
import java.io.File;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class InfoChatBot extends TelegramLongPollingBot {

  @Getter(onMethod_ = {@Override})
  private final String botUsername;
  @Getter(onMethod_ = {@Override})
  private final String botToken;

  @Autowired
  private BotSettings botSettings;
  @Autowired
  private MenuConfig menuConfig;
  @Autowired
  private OpenNLPConf openNLPConf;
  @Autowired
  private TelegramBotsApi telegramBotsApi;
  @Autowired
  private UserKeyboardService userKeyboardService;
  @Autowired
  private FileUtils fileUtils;

  private String mainKeyboard;

  public InfoChatBot(BotSettings botSettings) {
    this.botUsername = botSettings.getUsername();
    this.botToken = botSettings.getToken();
  }

  @PostConstruct
  private void init() throws TelegramApiException {
    telegramBotsApi.registerBot(this);

    if (menuConfig.getKeyboard(MenuConfig.START) != null) {
      mainKeyboard = MenuConfig.START;
    } else {
      KeyboardInfo keyboardInfo = menuConfig.getFirstKeyboard();
      if (keyboardInfo != null) {
        mainKeyboard = keyboardInfo.getName();
      }
    }
  }

  @Override
  public void onUpdateReceived(Update update) {
    Message message = update.getMessage();
    if (message != null) {
      try {
        processMessage(message);
      } catch (Exception e) {
        log.error("Error while processing message from chat {}", message.getChatId());
      }
    }
  }

  private void processMessage(Message message) throws TelegramApiException {
    String messageText = message.getText();
    String chatId = message.getChatId().toString();

    if (openNLPConf.isInitialized() && menuConfig.getAnswer(messageText) == null &&
            menuConfig.getKeyboard(messageText) == null) {
      processOpenNlpMessage(chatId, messageText);
    } else {
      processSimpleMessage(chatId, messageText);
    }
  }

  private void processOpenNlpMessage(String chatId, String messageText) throws TelegramApiException {
    String category = openNLPConf.getCategory(messageText);
    ReplyKeyboardMarkup replyKeyboardMarkup = getSendKeyboard(chatId, messageText);
    if (category == null) {
      execute(getSendMessage(chatId, openNLPConf.getNotFoundAnswer(), replyKeyboardMarkup));
    } else {
    processSimpleMessage(chatId, category);
    }
  }

  private void processSimpleMessage(String chatId, String messageText) throws TelegramApiException {
    ReplyKeyboardMarkup replyKeyboardMarkup = getSendKeyboard(chatId, messageText);
    String sendText = getSendText(messageText);
    File image = getImage(messageText);

    if (image != null && image.exists()) {
      execute(getSendPhoto(chatId, sendText, image, replyKeyboardMarkup));
    } else {
      execute(getSendMessage(chatId, sendText, replyKeyboardMarkup));
    }
  }

  private ReplyKeyboardMarkup getSendKeyboard(String chatId, String messageText) {
    UserKeyboardInfo userKeyboardInfo = userKeyboardService.findUserKeyboardInfo(chatId);
    KeyboardInfo currentKeyboard = getCurrentOrDefaultKeyboard(userKeyboardInfo);
    List<KeyboardRow> sendKeyboard;

    if (botSettings.getBackButton().equals(messageText) && !userKeyboardInfo.getPreviousKeyboards().isEmpty()) {
      sendKeyboard = getPreviousKeyboard(userKeyboardInfo);
    } else {
      sendKeyboard = getNextKeyboard(userKeyboardInfo, messageText, currentKeyboard);

      if (sendKeyboard == null && currentKeyboard != null){
        sendKeyboard = currentKeyboard.getKeyboard();
      }
    }

    ReplyKeyboardMarkup replyKeyboardMarkup = null;
    if (sendKeyboard != null) {
      if (!userKeyboardInfo.getPreviousKeyboards().isEmpty()) {
        sendKeyboard.add(new KeyboardRow(
            Collections.singleton(new KeyboardButton(botSettings.getBackButton()))));
      }

      replyKeyboardMarkup = new ReplyKeyboardMarkup();
      replyKeyboardMarkup.setKeyboard(sendKeyboard);
      replyKeyboardMarkup.setSelective(true);
      replyKeyboardMarkup.setResizeKeyboard(true);
      replyKeyboardMarkup.setOneTimeKeyboard(false);
    }
    return replyKeyboardMarkup;
  }

  private KeyboardInfo getCurrentOrDefaultKeyboard(UserKeyboardInfo userKeyboardInfo) {
    List<KeyboardRow> currentKeyboard = null;
    String keyboardName = null;

    if (userKeyboardInfo.getCurrentKeyboard() != null) {
      keyboardName = userKeyboardInfo.getCurrentKeyboard();
      currentKeyboard = menuConfig.getKeyboard(userKeyboardInfo.getCurrentKeyboard());
    }else if (mainKeyboard != null) {
      keyboardName = mainKeyboard;
      currentKeyboard = menuConfig.getKeyboard(mainKeyboard);
    }

    if (currentKeyboard != null) {
      return new KeyboardInfo(keyboardName, currentKeyboard);
    } else {
      return null;
    }
  }

  private List<KeyboardRow> getPreviousKeyboard(UserKeyboardInfo userKeyboardInfo) {
    String previousKeyboard = userKeyboardInfo.getPreviousKeyboards().pollLast();
    userKeyboardInfo.setCurrentKeyboard(previousKeyboard);
    userKeyboardService.updateUserKeyboardInfo(userKeyboardInfo);
    return menuConfig.getKeyboard(previousKeyboard);
  }

  private List<KeyboardRow> getNextKeyboard(UserKeyboardInfo userKeyboardInfo, String messageText, KeyboardInfo currentKeyboard) {
    List<KeyboardRow> nextKeyboard = null;
    if (messageText != null && !messageText.isEmpty()) {
      nextKeyboard = menuConfig.getKeyboard(messageText);
    }

    if (nextKeyboard != null) {
      userKeyboardInfo.setCurrentKeyboard(messageText);
      if (currentKeyboard!= null && !messageText.equals(currentKeyboard.getName())) {
        userKeyboardInfo.getPreviousKeyboards().add(currentKeyboard.getName());
      }

      userKeyboardService.updateUserKeyboardInfo(userKeyboardInfo);
    }

    return nextKeyboard;
  }

  private String getSendText(String messageText) {
    String sendText = null;
    if (messageText != null && !messageText.isEmpty()) {
      sendText = menuConfig.getAnswer(messageText);
    }
    return sendText;
  }

  private File getImage(String messageText) {
    File image = null;
    String imageName = null;
    if (messageText != null && !messageText.isEmpty()) {
      imageName = menuConfig.getImage(messageText);
    }

    if (imageName != null && !imageName.isEmpty()) {
      image = fileUtils.getImage(imageName);
    }
    return image;
  }

  private SendPhoto getSendPhoto(String chatId, String sendText, File image, ReplyKeyboardMarkup replyKeyboardMarkup) {
    SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(image));
    sendPhoto.setCaption(sendText);
    sendPhoto.setParseMode(ParseMode.MARKDOWN);

    if (replyKeyboardMarkup != null) {
      sendPhoto.setReplyMarkup(replyKeyboardMarkup);
    }
    return sendPhoto;
  }

  private SendMessage getSendMessage(String chatId, String sendText, ReplyKeyboardMarkup replyKeyboardMarkup) {
    SendMessage sendMessage;
    if (sendText != null) {
      sendMessage = new SendMessage(chatId, sendText);
    } else {
      sendMessage = new SendMessage(chatId, botSettings.getDefaultAnswer());
    }
    sendMessage.enableMarkdown(true);

    if (replyKeyboardMarkup != null) {
      sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
    return sendMessage;
  }

}
