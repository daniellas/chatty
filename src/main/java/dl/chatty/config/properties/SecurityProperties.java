package dl.chatty.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("chatty.security")
public class SecurityProperties {
    private String customerUsername;
    private String customerPassword;
    private String customerRole;
    private String employeeUsername;
    private String employeePassword;
    private String employeeRole;
    private String adminUsername;
    private String adminPassword;
    private String adminRole;
}
