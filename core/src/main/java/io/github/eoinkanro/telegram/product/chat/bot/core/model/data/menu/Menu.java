package io.github.eoinkanro.telegram.product.chat.bot.core.model.data.menu;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "Menu")
public class Menu {

  @XmlElementWrapper(name = "Keyboards")
  @XmlElement(name = "Keyboard")
  private List<Keyboard> keyboards = new ArrayList<>();

  @XmlElementWrapper(name = "Answers")
  @XmlElement(name = "Answer")
  private List<Answer> answers = new ArrayList<>();

}
