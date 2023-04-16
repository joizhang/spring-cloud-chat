package com.joizhang.chat.web.controller;

import com.joizhang.chat.web.api.vo.Greeting;
import com.joizhang.chat.web.api.vo.MessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/hello")
    public void greeting(Principal principal, MessageVo message) {
        message.setFrom(principal.getName());
        Greeting greeting = new Greeting(message.getContent());
        sendingOperations.convertAndSendToUser(message.getTo(), message.getDestination(), greeting);
    }

}
