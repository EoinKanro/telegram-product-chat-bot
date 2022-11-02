package io.github.eoinkanro.telegram.info.chat.bot.api.conf;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.nio.charset.StandardCharsets;
import java.util.Random;

@Slf4j
@Configuration
@EnableScheduling
public class TokenHolder {

    @Getter
    private String token;

    @Scheduled(fixedDelayString = "${telegram.api.tokenExpiry:60000}")
    private void generateToken() {
        byte[] array = new byte[20];
        new Random().nextBytes(array);
        token = new String(array, StandardCharsets.UTF_8);
        log.trace("Token updated");
    }
}
