package dl.chatty.chat.broker;

import java.util.Collection;

/**
 * Chat subscriptions management
 * 
 * @author Daniel Łaś
 *
 * @param <I>
 *            chat dentifier type
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
    void create(I chatId, String user, Collection<String> subscriptionIds);

    /**
     * Removes subscriptions for given user and subscription identifiers
     * 
     * @param user
     * @param subscriptionIds
     */
    void remove(String user, Collection<String> subscriptionIds);

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
    Collection<String> chatDestinations(I chatId);

    /**
     * Returns single personal destination for given user and chat identifier
     * 
     * @param chatId
     * @param user
     * @return chat destination
     */
    String chatUserDestination(I chatId, String user);
}
