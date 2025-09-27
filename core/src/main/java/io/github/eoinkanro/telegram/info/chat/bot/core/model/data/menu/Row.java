package io.github.eoinkanro.telegram.info.chat.bot.core.model.data.menu;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "Row")
public class Row {

  @XmlElementWrapper(name = "ButtonList")
  @XmlElement(name = "Button")
  private List<Button> buttonList = new ArrayList<>();
}
