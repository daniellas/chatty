package dl.chatty.security;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityContextCurrentUsernameSupplier implements CurrentUsernameSupplier {

    @Override
    public Optional<String> get() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .flatMap(i -> Optional.ofNullable(i.getAuthentication()))
                .map(i -> (UserDetails) i.getPrincipal())
                .map(UserDetails::getUsername);
    }

}
