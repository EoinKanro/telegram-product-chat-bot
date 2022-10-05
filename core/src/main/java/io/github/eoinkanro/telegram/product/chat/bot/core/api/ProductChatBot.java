package io.github.eoinkanro.telegram.product.chat.bot.core.api;

import io.github.eoinkanro.telegram.product.chat.bot.core.conf.BotSettings;
import io.github.eoinkanro.telegram.product.chat.bot.core.conf.MenuConfig;
import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.cache.UserKeyboard;
import io.github.eoinkanro.telegram.product.chat.bot.core.service.UserKeyboardService;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Component
public class ProductChatBot extends SpringWebhookBot{

  @Getter(onMethod_ = {@Override})
  private final String botUsername;
  @Getter(onMethod_ = {@Override})
  private final String botToken;
  @Getter(onMethod_ = {@Override})
  private final String botPath;
  private final String defaultAnswer;

  @Autowired
  private MenuConfig menuConfig;

  @Autowired
  private UserKeyboardService userKeyboardService;

  public ProductChatBot(SetWebhook setWebhook, BotSettings botSettings) {
    super(setWebhook);
    this.botUsername = botSettings.getUsername();
    this.botToken = botSettings.getToken();
    this.botPath = botSettings.getWebhookPath();
    this.defaultAnswer = botSettings.getDefaultAnswer();
  }

  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    Message message = update.getMessage();
    if (message != null) {
      return processMessage(message);
    }
    return null;
  }

  private BotApiMethodMessage processMessage(Message message) {
    String messageText = message.getText();

    UserKeyboard currentKeyboardCache = userKeyboardService.findCurrentKeyboard(message.getChatId().toString());
    List<KeyboardRow> currentKeyboard = getCurrentKeyboard(currentKeyboardCache);

    List<KeyboardRow> keyboard = null;
    String answer = null;

    if (messageText != null && !messageText.isEmpty()) {
      if (menuConfig.checkKeyboard(messageText)) {
        keyboard = menuConfig.getKeyboard(messageText);
      } else if (menuConfig.checkAnswer(messageText)) {
        answer = menuConfig.getAnswer(messageText);
      }
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

    SendMessage sendMessage;
    if (answer != null) {
      sendMessage = new SendMessage(message.getChatId().toString(), answer);
    } else {
      sendMessage = new SendMessage(message.getChatId().toString(), defaultAnswer);
    }

    if (replyKeyboardMarkup != null) {
      sendMessage.setReplyMarkup(replyKeyboardMarkup);
      sendMessage.enableMarkdown(true);
    }
    return sendMessage;
  }

  private List<KeyboardRow> getCurrentKeyboard(UserKeyboard currentKeyboardCache) {
    List<KeyboardRow> currentKeyboard;
    if (currentKeyboardCache.getKeyboardLink() != null && menuConfig.checkKeyboard(currentKeyboardCache.getKeyboardLink())) {
      currentKeyboard = menuConfig.getKeyboard(currentKeyboardCache.getKeyboardLink());
    } else if (menuConfig.checkKeyboard(MenuConfig.START)) {
      currentKeyboard = menuConfig.getKeyboard(MenuConfig.START);
    } else {
      currentKeyboard = menuConfig.getFirstKeyboard();
    }
    return currentKeyboard;
  }

}
