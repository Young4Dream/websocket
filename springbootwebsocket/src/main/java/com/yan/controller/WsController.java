package com.yan.controller;

import com.yan.model.WiselyMessage;
import com.yan.model.WiselyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WsController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WsController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public WiselyResponse say(WiselyMessage message) throws Exception {
        Thread.sleep(3000);
        return new WiselyResponse("Welcome, " + message.getName() + "!");
    }

    @MessageMapping("/chat")
    public void handleChat(Principal principal, String msg) {
        if ("yan".equals(principal.getName())) {
            simpMessagingTemplate.convertAndSendToUser("kong", "/queue/notifications", principal.getName() + "-send:" + msg);
        } else {
            simpMessagingTemplate.convertAndSendToUser("yan", "/queue/notifications", principal.getName() + "-send:" + msg);
        }
    }
}
