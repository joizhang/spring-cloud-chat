package com.joizhang.chat.web.controller;

import com.joizhang.chat.web.api.vo.Greeting;
import com.joizhang.chat.web.api.vo.MessageVo;
import com.joizhang.chat.web.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/hello")
    public void greeting(Principal principal, MessageVo message) {
        message.setFrom(principal.getName());
        Greeting greeting = new Greeting(message.getContent());
        chatMessageService.send("hello", "hello");
    }

}
