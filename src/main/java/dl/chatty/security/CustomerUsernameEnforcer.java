package dl.chatty.security;

import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerUsernameEnforcer implements UsernameEnforcer, SecurityRoleChecker {

    private final AuthenticationSupplier authenticationSupplier;

    @Override
    public String apply(String username) {
        Authentication auth = authenticationSupplier.getOrThrowIfAnonymous();

        return hasAuthority(auth, Roles.CUSTOMER)
                .map(a -> auth.getName())
                .orElse(username);
    }

}
