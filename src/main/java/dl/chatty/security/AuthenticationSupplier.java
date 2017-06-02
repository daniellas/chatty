package dl.chatty.security;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

/**
 * Provides {@link Authentication}
 * 
 * @author Daniel Łaś
 *
 */
public interface AuthenticationSupplier extends Supplier<Optional<Authentication>> {

    /**
     * Gets {@link Authentication} object
     * 
     * @return {@link Authentication} or throws
     *         {@link InsufficientAuthenticationException} if not present
     */
    default Authentication getOrThrowIfAnonymous() {
        return get()
                .filter(a -> !AnonymousAuthenticationToken.class.isAssignableFrom(a.getClass()))
                .orElseThrow(() -> {
                    return new InsufficientAuthenticationException("Anonymous authentication insufficient");
                });
    }
}
