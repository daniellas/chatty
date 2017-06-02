package dl.chatty.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Security roles verification
 * 
 * @author Daniel Łaś
 *
 */
public interface SecurityRoleChecker {

    /**
     * Checks if given auth has given role
     * 
     * @param auth
     *            to verify
     * @param role
     *            to check
     * @return {@link GrantedAuthority} {@link Optional} or empty if given auth
     *         does't have given role
     */
    default Optional<? extends GrantedAuthority> hasAuthority(Authentication auth, String role) {
        return auth.getAuthorities().stream()
                .filter(a -> a.getAuthority().equals(role))
                .findFirst();
    }
}
