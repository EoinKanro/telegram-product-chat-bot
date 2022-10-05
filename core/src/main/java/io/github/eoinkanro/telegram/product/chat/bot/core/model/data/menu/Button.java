package io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;

@Getter
@XmlRootElement(name = "Button")
public class Button {

  @XmlValue
  private String text;
}
