package dl.chatty.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextAuthenticationSupplier implements AuthenticationSupplier {

    @Override
    public Optional<Authentication> get() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .flatMap(i -> Optional.ofNullable(i.getAuthentication()));
    }

}
