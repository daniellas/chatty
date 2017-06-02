package dl.chatty.chat.broker;

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
    public void onSend(I chatId, M message, P sender, String sessionId);

    /**
     * Processes new chat subscription
     * 
     * @param subscriptionIds
     * @param destination
     * @param user
     */
    public void onSubscribe(String destination, P user, String sessionId);

    /**
     * Processes chat unsubscribe operation
     * 
     * @param subscriptionIds
     * @param user
     */
    public void onUnsubscribe(P user, String sessionId);

    /**
     * Processes user disconnection
     * 
     * @param user
     */
    public void onDisconnect(P user, String sessionId);

}
