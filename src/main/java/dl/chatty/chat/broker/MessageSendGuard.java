package dl.chatty.chat.broker;

import java.security.Principal;
import java.util.Optional;

import dl.chatty.chat.entity.Chat;

public interface MessageSendGuard {
    Optional<Chat> messageChat(String chatId,Principal user);
}
