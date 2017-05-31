package dl.chatty.chat.broker;

import java.util.List;

public interface Broker<I, M, P> {

    public void onSend(I chatId, M message, P sender);

    public void onSubscribe(List<String> subscriptionIds, I destination, P user);

    public void onUnsubscribe(List<String> subscriptionIds, P user);
    
    public void onDisconnect(P user);

}
