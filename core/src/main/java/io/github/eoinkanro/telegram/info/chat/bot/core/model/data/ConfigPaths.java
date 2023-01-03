package io.github.eoinkanro.telegram.info.chat.bot.core.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigPaths {

  CONFIG_FOLDER("config"),
  CACHE_FOLDER("cache"),
  IMAGE_FOLDER("img"),
  OPENNLP_FOLDER("nlp"),
  MENU_FILE("menu.xml"),
  CATEGORIZER_DICT("categorizer.txt"),
  SENTENCE_DICT("sentence.bin"),
  TOKENIZER_DICT("tokenizer.bin"),
  POSTAGGER_DICT("postagger.bin"),
  LEMMATIZER_DICT("lemmatizer.txt");

  private final String path;
}
