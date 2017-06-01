package dl.chatty.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Roles {
    public static final String CUSTOMER = "ROLE_CUSTOMER";
    public static final String EMPLOYEE = "ROLE_EMPLOYEE";
    public static final String ACTUATOR = "ROLE_ACTUATOR";
}
