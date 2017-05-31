package dl.chatty.security;

import java.security.Principal;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextCurrentUsernameSupplier implements CurrentUsernameSupplier {

    @Override
    public Optional<String> get() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .flatMap(i -> Optional.ofNullable(i.getAuthentication()))
                .map(i -> (Principal) i)
                .map(i -> i.getName());
    }

}
