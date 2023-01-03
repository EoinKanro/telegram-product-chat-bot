package io.github.eoinkanro.telegram.info.chat.bot.api.controller;

import io.github.eoinkanro.telegram.info.chat.bot.api.conf.AuthConf;
import io.github.eoinkanro.telegram.info.chat.bot.api.conf.TokenHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("localhost")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthConf authConf;

    @Autowired
    private TokenHolder tokenHolder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestHeader(name = "Username") String username, @RequestHeader(name = "Password")String password) {
        if (authConf.getUsername().equals(username) && authConf.getPassword().equals(password)) {
            return new ResponseEntity<>(tokenHolder.getToken(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/checkToken")
    public ResponseEntity<String> checkToken(@RequestBody String token) {
        if (tokenHolder.getToken().equals(token)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
