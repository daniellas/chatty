package dl.chatty.chat.repository;

import java.util.Collection;
import java.util.Optional;

import dl.chatty.chat.entity.Message;

public interface MessageRepository {

    Optional<Message> create(String chatId, Message sourceMessage, String sender);

    Collection<Message> findForChat(String chatId);
}
