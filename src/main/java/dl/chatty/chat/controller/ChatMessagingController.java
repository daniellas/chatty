package dl.chatty.chat.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import dl.chatty.chat.broker.Broker;
import dl.chatty.chat.broker.StompHeadersUtil;

@Controller
public class ChatMessagingController {

    @Autowired
    private Broker<Long, String, Principal> broker;

    @MessageMapping("/messages/{chatId}")
    public void handleMessage(@Payload String message, @DestinationVariable("chatId") Long chatId, Principal sender, Message<?> stompMessage) {
        broker.onSend(chatId, message, sender, StompHeadersUtil.sessionId(stompMessage));
    }

}
