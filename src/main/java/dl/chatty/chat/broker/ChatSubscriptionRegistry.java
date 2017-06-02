package dl.chatty.chat.broker;

import java.util.Collection;

/**
 * Chat subscriptions registry
 * 
 * @author Daniel Łaś
 *
 * @param <I>
 *            chat identifier type
 */
public interface ChatSubscriptionRegistry<I> {

    /**
     * Creates new subscription for given user, chat identifier, subscriptions
     * identifiers
     * 
     * @param chatId
     * @param user
     * @param subscriptionIds
     */
    void create(I chatId, String user, String sessionId);

    /**
     * Removes subscriptions for given user and subscription identifiers
     * 
     * @param user
     * @param subscriptionIds
     */
    void remove(String user, String sessionId);

    /**
     * Removes all subscriptions for given user
     * 
     * @param user
     */
    void remove(String user);

    /**
     * Resolves all chat destinations for given chat identifier based on active
     * chat/user subscriptions
     * 
     * @param chatId
     * @return all chat destinations
     */
    Collection<Subscription> chatSubscriptions(I chatId);

    public interface Subscription {
        String getUser();

        String getSessionId();
    }

}
