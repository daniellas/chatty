package dl.chatty.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry registry) {
        String[] destMacthers = Arrays.stream(WebSocketConfig.DESTINATION_PREFIXES).map(p -> p + "/*").toArray(String[]::new);

        registry.simpDestMatchers(destMacthers).authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
