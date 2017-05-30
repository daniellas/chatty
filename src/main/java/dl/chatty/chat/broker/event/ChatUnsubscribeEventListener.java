package dl.chatty.chat.broker.event;

import java.security.Principal;

import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import dl.chatty.chat.broker.Broker;
import dl.chatty.chat.broker.StompHeadersUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatUnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {

    private final Broker<String, ?, Principal> broker;

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        broker.onUnsubscribe(StompHeadersUtil.subscriptionIds(event.getMessage().getHeaders()),event.getUser());
    }

}
