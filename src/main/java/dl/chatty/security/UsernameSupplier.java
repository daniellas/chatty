package dl.chatty.security;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

/**
 * Current username access
 * 
 * @author Daniel Łaś
 *
 */
public interface UsernameSupplier extends Supplier<Optional<String>> {

    /**
     * Gets current username
     * 
     * @return current username or throws
     *         {@link AuthenticationCredentialsNotFoundException} if not present
     */
    default String getOrThrowNoCredentials() {
        return get().orElseThrow(() -> {
            return new AuthenticationCredentialsNotFoundException("Not authenticated");
        });
    }

    /**
     * Just get username from {@link Optional}
     * 
     * @return username from {@link Optional}, will throw
     *         {@link NoSuchElementException} if not present
     */
    default String justGet() {
        return get().get();
    }
}
