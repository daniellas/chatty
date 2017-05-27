package dl.chatty.chat.broker;

import java.security.Principal;

public interface Broker<I, M> {
    public void dispatch(I destination, M message, Principal sender);
}
