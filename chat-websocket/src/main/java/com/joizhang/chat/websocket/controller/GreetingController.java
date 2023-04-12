package com.joizhang.chat.websocket.controller;

import com.joizhang.chat.websocket.vo.Greeting;
import com.joizhang.chat.websocket.vo.MessageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/hello")
    public void greeting(MessageVo message) throws Exception {
        Thread.sleep(1000); // simulated delay
        Greeting greeting = new Greeting("Hello, " + message.getName() + "!");
        sendingOperations.convertAndSend(message.getDestination(), greeting);
    }

}
