package dl.chatty;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityTestUtil {

    public static final String CUSTOMER_USERNAME = "customer";
    public static final String EMPLOYEE_USERNAME = "employee";
    public static final String PASSWORD = "password";

    public static void setAuthentication(String username, String password, String... roles) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        username, password,
                        Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
    }

    public static void setAnonymousAuthentication(String username, String password, String... roles) {
        SecurityContextHolder.getContext()
                .setAuthentication(new AnonymousAuthenticationToken(
                        username, password,
                        Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
    }

    public static void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
