package dl.chatty.chat.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import dl.chatty.chat.broker.Broker;

@Controller
public class ChatMessageController {

    @Autowired
    private Broker<String, String> broker;

    @MessageMapping("/chat/{chatId}")
    public void handleMessage(@Payload String message, @DestinationVariable("chatId") String chatId, Principal sender) {
        broker.dispatch(chatId, message, sender);
    }
}
