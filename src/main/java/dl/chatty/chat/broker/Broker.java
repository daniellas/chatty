package dl.chatty.chat.broker;

import java.util.List;

/**
 * Chat broker responsible for message sending, chat subscriptions and
 * connections management
 * 
 * @author Daniel Łaś
 *
 * @param <I>
 *            chat id type
 * @param <M>
 *            message type
 * @param <P>
 *            sender type
 */
public interface Broker<I, M, P> {

    /**
     * Processes new message
     * 
     * @param chatId
     * @param message
     * @param sender
     */
    public void onSend(I chatId, M message, P sender);

    /**
     * Processes new chat subscription
     * 
     * @param subscriptionIds
     * @param destination
     * @param user
     */
    public void onSubscribe(List<String> subscriptionIds, String destination, P user);

    /**
     * Processes chat unsubscribe operation
     * 
     * @param subscriptionIds
     * @param user
     */
    public void onUnsubscribe(List<String> subscriptionIds, P user);

    /**
     * Processes user disconnection
     * 
     * @param user
     */
    public void onDisconnect(P user);

}
