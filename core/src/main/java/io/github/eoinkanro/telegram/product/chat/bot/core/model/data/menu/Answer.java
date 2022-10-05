package io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;

@Getter
@XmlRootElement(name = "Answer")
public class Answer {

  @XmlAttribute(name = "link")
  private String link;

  @XmlValue
  private String text;
}
