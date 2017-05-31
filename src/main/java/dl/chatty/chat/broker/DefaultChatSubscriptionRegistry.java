package dl.chatty.chat.broker;

import java.util.Arrays;
import java.util.Collection;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultChatSubscriptionRegistry implements ChatSubscriptionRegistry<String> {

    @Override
    public void subscribe(String chatId, String user, Collection<String> subscriptionIds) {
    }

    @Override
    public void unsubscribe(String user, Collection<String> subscriptionIds) {

    }

    @Override
    public void disconnect(String user) {

    }

    @Override
    public Collection<String> chatDestinations(String chatId) {
        return Arrays.asList("/" + chatId + "/customer");
    }

    @Override
    public String chatUserDestination(String chatId, String user) {
        return "/" + chatId + "/" + user;
    }

}
