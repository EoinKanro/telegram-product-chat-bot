package io.github.eoinkanro.telegram.product.chat.bot.core.service;

import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.cache.UserKeyboard;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserKeyboardService {

  public static final String CACHE_NAME = "cacheData";

  @Cacheable(cacheNames = CACHE_NAME, key = "#chatId")
  public UserKeyboard findCurrentKeyboard(String chatId) {
    UserKeyboard userKeyboard = new UserKeyboard();
    userKeyboard.setChatId(chatId);
     return userKeyboard;
  }

  @CachePut(cacheNames = CACHE_NAME, key = "#keyboard.chatId")
  public UserKeyboard updateCurrentKeyboard(UserKeyboard keyboard) {
    return keyboard;
  }

}
