package dl.chatty.chat.broker;

import java.util.Optional;

import dl.chatty.chat.entity.Chat;

/**
 * Performs message sending security checks
 * 
 * @author Daniel Łaś
 * 
 * @param <I>
 *            chat identifier type
 * @param <P>
 *            message sender type
 */
public interface MessageSendGuard<I, P> {
    /**
     * Verifies if given user is allowed to send message to given chat
     * 
     * @param chatId
     * @param user
     * @return chat {@link Optional} or empty if message sending is forbidden
     */
    Optional<Chat> messageChat(I chatId, P user);
}
