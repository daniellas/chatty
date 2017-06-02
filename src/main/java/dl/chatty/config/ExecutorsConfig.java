package dl.chatty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dl.chatty.concurrency.ConfigurableExecutorsProvider;
import dl.chatty.concurrency.ExecutorsProvider;

@Configuration
public class ExecutorsConfig {

    @Bean
    public ExecutorsProvider executorsProvider() {
        return new ConfigurableExecutorsProvider();
    }
}
