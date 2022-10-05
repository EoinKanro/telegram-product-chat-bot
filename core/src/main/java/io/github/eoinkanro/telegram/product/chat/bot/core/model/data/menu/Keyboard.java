package io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "Keyboard")
public class Keyboard {

  @XmlAttribute(name = "link")
  private String link;

  @XmlElementWrapper(name = "RowList")
  @XmlElement(name = "Row")
  private List<Row> rowList = new ArrayList<>();

}
