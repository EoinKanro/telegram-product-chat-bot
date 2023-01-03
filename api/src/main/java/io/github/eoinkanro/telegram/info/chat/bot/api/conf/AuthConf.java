package io.github.eoinkanro.telegram.info.chat.bot.api.conf;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AuthConf {

    @Value("${telegram.api.username}")
    private String username;

    @Value("${telegram.api.password}")
    private String password;

}
