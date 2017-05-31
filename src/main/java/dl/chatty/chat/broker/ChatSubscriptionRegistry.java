package dl.chatty.chat.broker;

import java.util.Collection;

public interface ChatSubscriptionRegistry<I> {

    void subscribe(I chatId, String user, Collection<String> subscriptionIds);

    void unsubscribe(String user, Collection<String> subscriptionIds);

    void disconnect(String user);

    Collection<String> chatDestinations(I chatId);

    String chatUserDestination(I chatId, String user);
}
