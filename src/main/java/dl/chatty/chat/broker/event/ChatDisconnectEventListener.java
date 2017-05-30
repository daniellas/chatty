package dl.chatty.chat.broker.event;

import java.security.Principal;

import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import dl.chatty.chat.broker.Broker;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final Broker<String, ?, Principal> broker;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        broker.onDisconnect(event.getUser());
    }

}
