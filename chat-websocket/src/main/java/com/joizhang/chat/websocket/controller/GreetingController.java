package com.joizhang.chat.websocket.controller;

import com.joizhang.chat.websocket.vo.Greeting;
import com.joizhang.chat.websocket.vo.MessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/hello")
    public void greeting(Principal principal, MessageVo message) {
        message.setFrom(principal.getName());
        Greeting greeting = new Greeting(message.getContent());
        sendingOperations.convertAndSendToUser(message.getTo(), message.getDestination(), greeting);
    }

}
