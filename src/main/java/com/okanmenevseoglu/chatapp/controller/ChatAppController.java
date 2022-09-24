package com.okanmenevseoglu.chatapp.controller;

import com.okanmenevseoglu.chatapp.dto.ChatMessageDTO;
import com.okanmenevseoglu.chatapp.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatAppController {
    @Autowired
    public ChatMessageService chatMessageService;

    //mapped as "app/sendMessage", because we set "setApplicationDestinationPrefixes" in WebSocketConfig.java
    @MessageMapping("/sendMessage")
    @SendTo("/topic/chat")
    public ChatMessageDTO chat(@Payload ChatMessageDTO chatMessage) {

        return chatMessageService.saveChatMessageToDB(chatMessage);
    }

    //mapped as "app/addUser"
    @MessageMapping("/addUser")
    @SendTo("/topic/chat")
    public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getChatUser());
        return chatMessage;
    }
}