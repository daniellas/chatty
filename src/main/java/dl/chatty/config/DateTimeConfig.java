package dl.chatty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dl.chatty.datetime.DateTimeSupplier;
import dl.chatty.datetime.DefaultDateTimeSupplier;

@Configuration
public class DateTimeConfig {

    @Bean
    public DateTimeSupplier dateTimeProvider() {
        return new DefaultDateTimeSupplier();
    }

}
