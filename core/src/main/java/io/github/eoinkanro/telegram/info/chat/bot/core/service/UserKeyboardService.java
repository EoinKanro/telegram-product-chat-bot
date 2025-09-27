package io.github.eoinkanro.telegram.info.chat.bot.core.service;

import io.github.eoinkanro.telegram.info.chat.bot.core.model.data.cache.UserKeyboardInfo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserKeyboardService {

  public static final String CACHE_NAME = "cacheData";

  @Cacheable(cacheNames = CACHE_NAME, key = "#chatId")
  public UserKeyboardInfo findUserKeyboardInfo(String chatId) {
    UserKeyboardInfo userKeyboardInfo = new UserKeyboardInfo();
    userKeyboardInfo.setChatId(chatId);
     return userKeyboardInfo;
  }

  @CachePut(cacheNames = CACHE_NAME, key = "#keyboard.chatId")
  public UserKeyboardInfo updateUserKeyboardInfo(UserKeyboardInfo keyboard) {
    return keyboard;
  }

}
