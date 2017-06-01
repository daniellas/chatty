package dl.chatty.chat.broker.event;

import java.security.Principal;

import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import dl.chatty.chat.broker.Broker;
import dl.chatty.chat.broker.StompHeadersUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    private final Broker<Long, ?, Principal> broker;

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        broker.onSubscribe(
                StompHeadersUtil.subscriptionIds(event.getMessage().getHeaders()),
                StompHeadersUtil.destination(event.getMessage().getHeaders()),
                event.getUser());
    }

}
