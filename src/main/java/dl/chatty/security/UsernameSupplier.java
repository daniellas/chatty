package dl.chatty.security;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

public interface UsernameSupplier extends Supplier<Optional<String>> {

    default String getOrThrowNoCredentials() {
        return get().orElseThrow(() -> {
            return new AuthenticationCredentialsNotFoundException("Not authenticated");
        });
    }

    default String justGet() {
        return get().get();
    }
}
