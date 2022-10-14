package io.github.eoinkanro.telegram.info.chat.bot.core.utils;

import io.github.eoinkanro.telegram.info.chat.bot.core.model.data.ConfigPaths;
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
        + ConfigPaths.CONFIG_FOLDER.getPath()
        + File.separator
        + fileName);
  }

  public File getOpenNlpFile(String fileName) {
    return new File(getInnerConfFolder(ConfigPaths.OPENNLP_FOLDER.getPath())
            + fileName);
  }

  public File getImage(String fileName) {
    return new File(getInnerConfFolder(ConfigPaths.IMAGE_FOLDER.getPath())
        + fileName);
  }

  private String getInnerConfFolder(String folder) {
    return getCurrentPath() +
            File.separator
            + ConfigPaths.CONFIG_FOLDER.getPath()
            + File.separator
            + folder
            + File.separator;
  }

}
