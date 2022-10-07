package io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Getter;

@Getter
@XmlTransient
public abstract class MenuMainBlock {

  @XmlAttribute(name = "link")
  private String link;

}
