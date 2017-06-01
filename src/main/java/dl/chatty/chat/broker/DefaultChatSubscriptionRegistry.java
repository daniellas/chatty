package dl.chatty.chat.broker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import dl.chatty.concurrency.Locker;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class DefaultChatSubscriptionRegistry implements ChatSubscriptionRegistry<String> {

    private static final String SEPARATOR = "/";

    private final Set<Subscription> subscriptions = new HashSet<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void create(String chatId, String user, Collection<String> subscriptionIds) {
        Locker.lockWrite(() -> {
            subscriptionIds.stream()
                    .map(s -> Subscription.of(chatId, user, s))
                    .distinct()
                    .forEach(subscriptions::add);
        }, lock);
    }

    @Override
    public void remove(String user, Collection<String> subscriptionIds) {
        Locker.lockWrite(() -> {
            Set<Subscription> remove = subscriptions.stream()
                    .filter(s -> s.getUser().equals(user))
                    .filter(s -> subscriptionIds.contains(s.getSubscriptionId()))
                    .collect(Collectors.toSet());

            subscriptions.removeAll(remove);
        }, lock);
    }

    @Override
    public void remove(String user) {
        Locker.lockWrite(() -> {
            Set<Subscription> remove = subscriptions.stream()
                    .filter(s -> s.getUser().equals(user))
                    .collect(Collectors.toSet());

            subscriptions.removeAll(remove);

        }, lock);
    }

    @Override
    public Collection<String> chatDestinations(String chatId) {
        return Locker.lockRead(() -> {
            return subscriptions.stream()
                    .filter(s -> s.getChatId().equals(chatId))
                    .map(s -> SEPARATOR + chatId + SEPARATOR + s.getUser())
                    .collect(Collectors.toSet());

        }, lock);
    }

    @Override
    public String chatUserDestination(String chatId, String user) {
        return SEPARATOR + chatId + SEPARATOR + user;
    }

    @Data
    @RequiredArgsConstructor(staticName = "of")
    static class Subscription {
        private final String chatId;
        private final String user;
        private final String subscriptionId;
    }

}
