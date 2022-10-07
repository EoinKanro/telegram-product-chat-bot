package io.github.eoinkanro.telegram.product.chat.bot.core.api;

import io.github.eoinkanro.telegram.product.chat.bot.core.conf.BotSettings;
import io.github.eoinkanro.telegram.product.chat.bot.core.conf.MenuConfig;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.cache.UserKeyboard;
import io.github.eoinkanro.telegram.product.chat.bot.core.service.UserKeyboardService;
import io.github.eoinkanro.telegram.product.chat.bot.core.utils.FileUtils;
import java.io.File;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class ProductChatBot extends TelegramLongPollingBot {

  @Getter(onMethod_ = {@Override})
  private final String botUsername;
  @Getter(onMethod_ = {@Override})
  private final String botToken;
  private final String defaultAnswer;

  @Autowired
  private MenuConfig menuConfig;
  @Autowired
  private TelegramBotsApi telegramBotsApi;
  @Autowired
  private UserKeyboardService userKeyboardService;
  @Autowired
  private FileUtils fileUtils;

  public ProductChatBot(BotSettings botSettings) {
    this.botUsername = botSettings.getUsername();
    this.botToken = botSettings.getToken();
    this.defaultAnswer = botSettings.getDefaultAnswer();
  }

  @PostConstruct
  private void init() throws TelegramApiException {
    telegramBotsApi.registerBot(this);
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
    String chatId = message.getChatId().toString();
    String messageText = message.getText();

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
    UserKeyboard currentKeyboardCache = userKeyboardService.findCurrentKeyboard(chatId);
    List<KeyboardRow> currentKeyboard = getCurrentKeyboard(currentKeyboardCache);

    List<KeyboardRow> keyboard = null;
    if (messageText != null && !messageText.isEmpty()) {
      keyboard = menuConfig.getKeyboard(messageText);
    }

    ReplyKeyboardMarkup replyKeyboardMarkup = null;
    if (keyboard != null) {
      currentKeyboardCache.setKeyboardLink(messageText);
      userKeyboardService.updateCurrentKeyboard(currentKeyboardCache);
    } else {
      keyboard = currentKeyboard;
    }

    if (keyboard != null) {
      replyKeyboardMarkup = new ReplyKeyboardMarkup();
      replyKeyboardMarkup.setKeyboard(keyboard);
      replyKeyboardMarkup.setSelective(true);
      replyKeyboardMarkup.setResizeKeyboard(true);
      replyKeyboardMarkup.setOneTimeKeyboard(false);
    }
    return replyKeyboardMarkup;
  }

  private List<KeyboardRow> getCurrentKeyboard(UserKeyboard currentKeyboardCache) {
    List<KeyboardRow> currentKeyboard = null;
    if (currentKeyboardCache.getKeyboardLink() != null) {
      currentKeyboard = menuConfig.getKeyboard(currentKeyboardCache.getKeyboardLink());
    }
    if (currentKeyboard == null) {
      currentKeyboard = menuConfig.getKeyboard(MenuConfig.START);
    }
    if (currentKeyboard == null) {
      currentKeyboard = menuConfig.getFirstKeyboard();
    }
    return currentKeyboard;
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
      sendMessage = new SendMessage(chatId, defaultAnswer);
    }
    sendMessage.enableMarkdown(true);

    if (replyKeyboardMarkup != null) {
      sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
    return sendMessage;
  }

}
