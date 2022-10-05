package io.github.eoinkanro.telegram.product.chat.bot.core.utils;

import io.github.eoinkanro.telegram.product.chat.bot.core.model.data.ConfigPaths;
import java.io.File;
import org.springframework.stereotype.Component;

@Component
public class FileUtils {

  public String getCurrentPath() {
    return System.getProperty("user.dir");
  }

  public File getConfFile(String fileName) {
    return new File(getCurrentPath()
        + File.separator
        + ConfigPaths.CONFIG_PATH.getPath()
        + File.separator
        + fileName);
  }

}
