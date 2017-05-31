package dl.chatty;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityTestUtil {

    public static void setAuthentication(String username, String password, String ...roles) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        username, password,
                        Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
    }

    public static void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
