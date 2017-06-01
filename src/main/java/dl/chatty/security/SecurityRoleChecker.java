package dl.chatty.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public interface SecurityRoleChecker {
    default Optional<? extends GrantedAuthority> hasAuthority(Authentication auth, String role) {
        return auth.getAuthorities().stream()
                .filter(a -> a.getAuthority().equals(role))
                .findFirst();
    }
}
