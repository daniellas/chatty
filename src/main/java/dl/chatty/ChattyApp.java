package dl.chatty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dl.chatty.chat.controller.ChatMessagingController;
import dl.chatty.config.WebSocketConfig;

@SpringBootApplication(scanBasePackageClasses = { WebSocketConfig.class, ChatMessagingController.class })
public class ChattyApp {

    public static void main(String[] args) {
        SpringApplication.run(ChattyApp.class, args);
    }


}
