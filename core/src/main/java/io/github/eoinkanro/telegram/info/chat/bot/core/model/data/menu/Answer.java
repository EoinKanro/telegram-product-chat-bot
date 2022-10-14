package io.github.eoinkanro.telegram.info.chat.bot.core.model.data.menu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import lombok.Getter;

@Getter
@XmlRootElement(name = "Answer")
public class Answer extends MenuMainBlock{

  @XmlAttribute(name = "img")
  private String img;

  @XmlValue
  private String text;

}
