package dl.chatty.security;

import java.security.Principal;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecurityContextUsernameSupplier implements UsernameSupplier {

    private final AuthenticationSupplier authenticationSupplier;
    
    @Override
    public Optional<String> get() {
        return authenticationSupplier.get()
                .map(i -> (Principal) i)
                .map(i -> i.getName());
    }

}
